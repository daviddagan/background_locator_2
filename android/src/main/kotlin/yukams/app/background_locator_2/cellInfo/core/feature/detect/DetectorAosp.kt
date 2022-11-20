package yukams.app.background_locator_2.cellInfo.core.feature.detect

import android.Manifest
import androidx.annotation.RequiresPermission
import yukams.app.background_locator_2.cellInfo.core.INetMonster
import yukams.app.background_locator_2.cellInfo.core.db.model.NetworkType
import yukams.app.background_locator_2.cellInfo.core.telephony.ITelephonyManagerCompat

/**
 * Detection of current network type base of AOSP method.
 */
class DetectorAosp : INetworkDetector {

    @RequiresPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
    override fun detect(netmonster: INetMonster, telephony: ITelephonyManagerCompat): NetworkType =
        telephony.getNetworkType()


}