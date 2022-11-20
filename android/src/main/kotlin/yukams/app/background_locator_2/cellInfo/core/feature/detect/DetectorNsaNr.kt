package yukams.app.background_locator_2.cellInfo.core.feature.detect

import android.Manifest
import android.telephony.ServiceState
import androidx.annotation.RequiresPermission
import cz.mroczis.netmonster.core.INetMonster
import yukams.app.background_locator_2.cellInfo.core.db.model.NetworkType
import yukams.app.background_locator_2.cellInfo.core.feature.NrNsaStateParser
import yukams.app.background_locator_2.cellInfo.core.telephony.ITelephonyManagerCompat

/**
 * Attempts to detect NR in NSA mode
 *
 * Based on [ServiceState]'s contents added in Android P and currently active cells
 */
class DetectorNsaNr : INetworkDetector {
    private val nsaNrParser = NrNsaStateParser()

    @RequiresPermission(
        allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE]
    )
    override fun detect(netmonster: INetMonster, telephony: ITelephonyManagerCompat): NetworkType.Nr.Nsa? {
        val model = nsaNrParser.parse(netmonster, telephony)
        return if (model?.nrAvailable == true) {
            NetworkType.Nr.Nsa(NetworkType.LTE_NR, model)
        } else null
    }
}