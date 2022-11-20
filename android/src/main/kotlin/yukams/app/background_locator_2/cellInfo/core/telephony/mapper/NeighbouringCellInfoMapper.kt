package yukams.app.background_locator_2.cellInfo.core.telephony.mapper

import android.os.Build
import android.telephony.NeighboringCellInfo
import android.telephony.TelephonyManager
import yukams.app.background_locator_2.cellInfo.core.db.NetworkTypeTable
import yukams.app.background_locator_2.cellInfo.core.db.model.NetworkType
import yukams.app.background_locator_2.cellInfo.core.model.Network
import yukams.app.background_locator_2.cellInfo.core.model.annotation.TillSdk
import yukams.app.background_locator_2.cellInfo.core.model.cell.CellGsm
import yukams.app.background_locator_2.cellInfo.core.model.cell.CellWcdma
import yukams.app.background_locator_2.cellInfo.core.model.cell.ICell
import yukams.app.background_locator_2.cellInfo.core.model.connection.NoneConnection
import yukams.app.background_locator_2.cellInfo.core.model.signal.SignalGsm
import yukams.app.background_locator_2.cellInfo.core.model.signal.SignalWcdma
import yukams.app.background_locator_2.cellInfo.core.util.getGsmRssi
import yukams.app.background_locator_2.cellInfo.core.util.inRangeOrNull

/**
 * Neighbouring cell info which was deprecated in [Build.VERSION_CODES.M]
 * and direct call to retrieve instances of [NeighboringCellInfo] removed from SDK in [Build.VERSION_CODES.Q].
 *
 * Supports only [ICell] subset:
 *  - [yukams.app.background_locator_2.cellInfo.core.model.cell.CellGsm]
 *  - [yukams.app.background_locator_2.cellInfo.core.model.cell.CellWcdma]
 */
@TillSdk(
    sdkInt = Build.VERSION_CODES.Q,
    fallbackBehaviour = "This class is usable till Q cause required method was removed from SDK"
)
@Suppress("DEPRECATION")
class NeighbouringCellInfoMapper(
    private val telephony: TelephonyManager,
    private val subId: Int
) : ICellMapper<List<NeighboringCellInfo>?> {

    override fun map(model: List<NeighboringCellInfo>?): List<ICell> {
        val plmn = Network.map(telephony.networkOperator)
        return model?.mapNotNull {
            when(NetworkTypeTable.get(telephony.networkType)) {
                is NetworkType.Gsm -> processGsm(it, plmn)
                is NetworkType.Wcdma -> processWcdma(it, plmn)
                // Phones report just RSSI which is useless for us
                is NetworkType.Lte -> null
                // Other types were added to SDK when this method was already deprecated
                // or contents of NeighboringCellInfo do not match requirements (CDMA does not have CID, ...)
                else -> null
            }
        } ?: emptyList()
    }

    private fun processGsm(it: NeighboringCellInfo, plmn: Network?): ICell? {
        val cid = it.cid.inRangeOrNull(CellGsm.CID_RANGE)
        val lac = it.lac.inRangeOrNull(CellGsm.LAC_RANGE)
        val rssi = it.getGsmRssi()?.inRangeOrNull(SignalGsm.RSSI_RANGE)

        return if (cid != null && lac != null) {
            CellGsm(
                network = plmn,
                cid = cid,
                lac = lac,
                bsic = null,
                band = null,
                signal = SignalGsm(rssi, null, null),
                connectionStatus = NoneConnection(),
                subscriptionId = subId,
                timestamp = null
            )
        } else null
    }

    private fun processWcdma(it: NeighboringCellInfo, plmn: Network?) : ICell? {
        val psc = it.psc.inRangeOrNull(CellWcdma.PSC_RANGE)
        val rssi = it.rssi.inRangeOrNull(SignalWcdma.RSSI_RANGE)

        return if (psc != null) {
            CellWcdma(
                network = plmn,
                ci = null,
                lac = null,
                psc = psc,
                band = null,
                signal = SignalWcdma(rssi, null, null, null, null),
                connectionStatus = NoneConnection(),
                subscriptionId = subId,
                timestamp = null
            )
        } else null
    }
}