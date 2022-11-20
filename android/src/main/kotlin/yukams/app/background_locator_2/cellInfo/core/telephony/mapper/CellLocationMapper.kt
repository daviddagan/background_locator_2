package yukams.app.background_locator_2.cellInfo.core.telephony.mapper

import android.Manifest
import android.os.Build
import android.telephony.*
import android.telephony.cdma.CdmaCellLocation
import android.telephony.gsm.GsmCellLocation
import androidx.annotation.RequiresPermission
import androidx.annotation.WorkerThread
import yukams.app.background_locator_2.cellInfo.core.db.NetworkTypeTable
import yukams.app.background_locator_2.cellInfo.core.db.model.NetworkType
import yukams.app.background_locator_2.cellInfo.core.feature.config.CellLocationSource
import yukams.app.background_locator_2.cellInfo.core.feature.config.SignalStrengthsSource
import yukams.app.background_locator_2.cellInfo.core.model.Network
import yukams.app.background_locator_2.cellInfo.core.model.cell.CellGsm
import yukams.app.background_locator_2.cellInfo.core.model.cell.CellWcdma
import yukams.app.background_locator_2.cellInfo.core.model.cell.ICell
import yukams.app.background_locator_2.cellInfo.core.model.signal.SignalLte
import yukams.app.background_locator_2.cellInfo.core.model.signal.SignalWcdma
import yukams.app.background_locator_2.cellInfo.core.telephony.mapper.cell.mapCdma
import yukams.app.background_locator_2.cellInfo.core.telephony.mapper.cell.mapGsm
import yukams.app.background_locator_2.cellInfo.core.telephony.mapper.cell.mapLte
import yukams.app.background_locator_2.cellInfo.core.telephony.mapper.cell.mapWcdma
import yukams.app.background_locator_2.cellInfo.core.util.Reflection
import yukams.app.background_locator_2.cellInfo.core.util.inRangeOrNull

/**
 * Transforms [TelephonyManager.getCellLocation] into our representation.
 *
 * In older days signal, identity and PLMN info were separated into pieces and most of terminals
 * did not update them synchronously which led to false-positive results.
 * This class attempts to lower the overall error ratio.
 *
 * Generally I recommend to avoid using this data source when terminal has [android.os.Build.VERSION_CODES.N] or
 * newer -> it's better to rely on [CellInfoMapper].
 */
class CellLocationMapper(
    private val telephony: TelephonyManager,
    private val cellLocationSource: CellLocationSource,
    private val signalStrengthSource: SignalStrengthsSource,
    private val getNetworkOperator: () -> Network?
) : ICellMapper<Int> {


    /**
     * Maps [CellLocation] to our format using multiple other methods from [TelephonyManager].
     * This method is blocking and processing might take ~500 ms on devices with Android O and older.
     * The processing is a bit faster on other devices with Android P+ and newer.
     */
    @WorkerThread
    @RequiresPermission(allOf = [Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun map(model: Int): List<ICell> {
        val scanResult = getUpdatedLocationAndSignal(model)

        return mutableListOf<ICell>().apply {
            if (scanResult.location is GsmCellLocation) {
                map(scanResult.location, scanResult.signal, model)?.let { add(it) }
            } else if (scanResult.location is CdmaCellLocation) {
                scanResult.location.mapCdma(model, scanResult.signal)?.let { add(it) }
            }
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.READ_PHONE_STATE])
    private fun map(model: GsmCellLocation, signalStrength: SignalStrength?, subId: Int): ICell? {
        val network = NetworkTypeTable.get(telephony.networkType)
        val cid = model.cid
        val plmn = getNetworkOperator.invoke()

        val rsrp = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            signalStrength?.getCellSignalStrengths(CellSignalStrengthLte::class.java)
                ?.firstOrNull()
                ?.rsrp?.toDouble()?.inRangeOrNull(SignalLte.RSRP_RANGE)
        } else {
            Reflection.intFieldOrNull(Reflection.SS_LTE_RSRP, signalStrength)
                ?.toDouble()?.inRangeOrNull(SignalLte.RSRP_RANGE)
        }
        val wcdma = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            signalStrength?.getCellSignalStrengths(CellSignalStrengthWcdma::class.java)
                ?.firstOrNull()
                ?.dbm?.toLong()
        } else {
            Reflection.intFieldOrNull(Reflection.UMTS_RSCP, signalStrength)?.toLong()
        }

        return if (rsrp != null && network is NetworkType.Lte && !CellGsm.CID_RANGE.contains(cid)) {
            model.mapLte(subId, signalStrength, plmn)
        } else if (SignalWcdma.RSCP_RANGE.contains(wcdma) && network is NetworkType.Wcdma) {
            model.mapWcdma(subId, signalStrength, plmn)
        } else if (CellGsm.CID_RANGE.contains(cid) && (!CellWcdma.PSC_RANGE.contains(model.psc) || network is NetworkType.Gsm)) {
            model.mapGsm(subId, signalStrength, plmn)
        } else if (network is NetworkType.Lte || model.psc == 0) {
            model.mapLte(subId, signalStrength, plmn)
        } else if (network is NetworkType.Wcdma || CellWcdma.PSC_RANGE.contains(model.psc)) {
            model.mapWcdma(subId, signalStrength, plmn)
        } else {
            null
        }

    }

    /**
     * Attempts to fetch [SignalStrength] & [CellLocation] from system.
     * It might take a while depending on current OS version.
     */
    @WorkerThread
    @RequiresPermission(allOf = [Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION])
    private fun getUpdatedLocationAndSignal(subId: Int?): ScanResult =
        ScanResult(
            location = cellLocationSource.get(telephony, subId),
            signal = signalStrengthSource.get(telephony, subId)
        )

    /**
     * Wrapper for two instances we need to construct [ICell]
     */
    private data class ScanResult(
        val location: CellLocation?,
        val signal: SignalStrength?
    )

}