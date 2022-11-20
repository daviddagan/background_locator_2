package yukams.app.background_locator_2.cellInfo.core.subscription.mapper

import android.annotation.SuppressLint
import android.os.Build
import android.telephony.SubscriptionInfo
import yukams.app.background_locator_2.cellInfo.core.model.Network

/**
 * Grabs [Network] from [SubscriptionInfo]
 */
@SuppressLint("NewApi")
internal fun SubscriptionInfo.mapNetwork(): Network? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        Network.map(mccString, mncString)
    } else {
        Network.map(mcc, mnc)
    }