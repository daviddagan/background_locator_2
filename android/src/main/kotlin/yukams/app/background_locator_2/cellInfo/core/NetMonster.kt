package cz.mroczis.netmonster.core

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.annotation.WorkerThread
import yukams.app.background_locator_2.cellInfo.core.db.NetworkTypeTable
import yukams.app.background_locator_2.cellInfo.core.db.local.ILocalStorage
import yukams.app.background_locator_2.cellInfo.core.db.model.NetworkType
import yukams.app.background_locator_2.cellInfo.core.factory.LocalStorageFactory
import yukams.app.background_locator_2.cellInfo.core.factory.NetMonsterFactory
import yukams.app.background_locator_2.cellInfo.core.feature.config.PhysicalChannelConfigSource
import yukams.app.background_locator_2.cellInfo.core.feature.detect.*
import yukams.app.background_locator_2.cellInfo.core.feature.merge.CellMerger
import yukams.app.background_locator_2.cellInfo.core.feature.merge.CellNetworkRegistrationMerger
import yukams.app.background_locator_2.cellInfo.core.feature.merge.CellSignalMerger
import yukams.app.background_locator_2.cellInfo.core.feature.merge.CellSource
import yukams.app.background_locator_2.cellInfo.core.feature.postprocess.*
import yukams.app.background_locator_2.cellInfo.core.model.NetMonsterConfig
import yukams.app.background_locator_2.cellInfo.core.model.cell.ICell
import yukams.app.background_locator_2.cellInfo.core.model.config.PhysicalChannelConfig
import yukams.app.background_locator_2.cellInfo.core.subscription.ISubscriptionManagerCompat
import yukams.app.background_locator_2.cellInfo.core.telephony.ITelephonyManagerCompat
import yukams.app.background_locator_2.cellInfo.core.telephony.mapper.cell.toCells
import yukams.app.background_locator_2.cellInfo.core.util.isDisplayOn

internal class NetMonster(
    private val context: Context,
    private val subscription: ISubscriptionManagerCompat,
    config: NetMonsterConfig
) : INetMonster {

    private val oldAndNewCellMerger = CellMerger()
    private val signalMerger = CellSignalMerger()
    private val networkRegistrationMerger = CellNetworkRegistrationMerger()
    private val physicalChannelSource by lazy { PhysicalChannelConfigSource() }
    private val storage: ILocalStorage = LocalStorageFactory.get(context, config)

    /**
     * Postprocessors that try to fix / add behaviour to [ITelephonyManagerCompat.getAllCellInfo]
     */
    @SuppressLint("MissingPermission")
    private val postprocessors = listOf(
        SamsungInvalidValuesPostprocessor(),
        MocnNetworkPostprocessor(
            subscription = subscription,
            networkOperatorGetter = { subId -> getTelephony(subId).getNetworkOperator() },
            serviceStateGetter = { subId -> getTelephony(subId).getServiceState() },
        ), // fix PLMNs
        InvalidCellsPostprocessor(), // get rid of false-positive cells
        PrimaryCellPostprocessor(), // mark 1st cell as Primary if required
        SubDuplicitiesPostprocessor(subscription) { subId ->
            getTelephony(subId).getNetworkOperator()
        }, // filter out duplicities, only Dual SIMs
        PlmnPostprocessor(), // guess PLMNs when channels match
        CdmaPlmnPostprocessor(), // guess PLMN for CDMA cells
        SignalStrengthPostprocessor { subId ->
            getTelephony(subId).getCellLocation().firstOrNull()
        }, // might add more signal strength indicators
        CellBandwidthPostprocessor(
            serviceStateGetter = { subId -> getTelephony(subId).getServiceState() },
            onCellWithDirectBandwidth = { storage.reportsLteBandwidthDirectly = true },
            isEnabled = { !storage.reportsLteBandwidthDirectly }
        ) ,
        InvalidSecondaryCellsPostprocessor(),
        PhysicalChannelPostprocessor { subId ->
            getPhysicalChannelConfiguration(subId)
        },
        SamsungEndiannessPostprocessor(
            getCellSkeleton = { getTelephony(it).getTelephonyManager().cellSkeleton },
            setEnabled = { storage.locationAreaEndiannessIncorrect = true },
            isEnabled = { storage.locationAreaEndiannessIncorrect },
        ),
    )

    @WorkerThread
    @RequiresPermission(
        allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE]
    )
    override fun getCells(): List<ICell> = getCells(
        CellSource.CELL_LOCATION,
        CellSource.ALL_CELL_INFO,
        CellSource.NEIGHBOURING_CELLS,
        CellSource.SIGNAL_STRENGTH,
        CellSource.NETWORK_REGISTRATION_INFO,
    )

    @WorkerThread
    @RequiresPermission(
        allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE]
    )
    override fun getCells(vararg sources: CellSource): List<ICell> {
        val subscriptions = subscription.getActiveSubscriptionIds()
        val oldApi = mutableListOf<ICell>().apply {
            if (CellSource.CELL_LOCATION in sources) {
                val serving = subscriptions.map { subId ->
                    getTelephony(subId).getCellLocation()
                }.flatten().toSet()

                addAll(serving)
            }

            if (CellSource.NEIGHBOURING_CELLS in sources) {
                val neighbouring = subscriptions.map { subId ->
                    getTelephony(subId).getNeighboringCellInfo()
                }.flatten().toSet()

                addAll(neighbouring)
            }
        }

        val newApi = if (CellSource.ALL_CELL_INFO in sources) {
            var allCells = subscriptions.map { subId ->
                getTelephony(subId).getAllCellInfo()
            }.flatten().toSet().toList()

            postprocessors.forEach { allCells = it.postprocess(allCells) }
            allCells
        } else emptyList()

        val signalApi = if (CellSource.SIGNAL_STRENGTH in sources) {
            subscriptions.mapNotNull { subId ->
                getTelephony(subId).getSignalStrength()?.toCells(subId)
            }.flatten().toSet().toList()
        } else emptyList()

        val networkRegistrationApi = if (CellSource.NETWORK_REGISTRATION_INFO in sources) {
            subscriptions.mapNotNull { subId ->
                getTelephony(subId).getServiceState()?.toCells(subId)
            }.flatten().toSet().toList()

        } else emptyList()

        val mergedOldNew = oldAndNewCellMerger.merge(oldApi, newApi, context.isDisplayOn(), subscriptions)
        val mergedWithSignal = signalMerger.merge(mergedOldNew, signalApi)
        return networkRegistrationMerger.merge(mergedWithSignal, networkRegistrationApi)
    }

    @RequiresPermission(
        allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE]
    )
    override fun getNetworkType(subId: Int): NetworkType = getNetworkType(
        subId,
        DetectorHspaDc(),
        DetectorLteAdvancedNrServiceState(),
        DetectorLteAdvancedPhysicalChannel(),
        DetectorLteAdvancedCellInfo(),
        DetectorAosp() // best to keep last when all other strategies fail
    ) ?: NetworkTypeTable.get(NetworkType.UNKNOWN)

    override fun getNetworkType(subId: Int, vararg detectors: INetworkDetector): NetworkType? {
        val telephony = getTelephony(subId)
        return detectors.firstNotNullOfOrNull { detector ->
            detector.detect(this, telephony)
        }
    }

    override fun getPhysicalChannelConfiguration(subId: Int): List<PhysicalChannelConfig> =
        getTelephony(subId).getTelephonyManager()?.let {
            physicalChannelSource.get(it, subId)
        } ?: emptyList()

    private fun getTelephony(subId: Int): ITelephonyManagerCompat {
        return NetMonsterFactory.getTelephony(context, subId)
    }

}