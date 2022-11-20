package yukams.app.background_locator_2.cellInfo.core.subscription

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import yukams.app.background_locator_2.cellInfo.core.model.SubscribedNetwork

internal open class SubscriptionManagerCompat14(
    internal val context: Context
) : ISubscriptionManagerCompat {

    /**
     * No dual SIM support in Android SDK prior Lollipop 5.1
     */
    @RequiresPermission(allOf = [Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun getActiveSubscriptionIds(): List<Int> = mutableListOf<Int>().apply {
        add(Integer.MAX_VALUE)
    }

    /**
     * No dual SIM support in Android SDK prior Lollipop 5.1
     */
    @RequiresPermission(allOf = [Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun getActiveSubscriptions(): List<SubscribedNetwork> = mutableListOf<SubscribedNetwork>().apply {
        add(SubscribedNetwork(0, Integer.MAX_VALUE, null))
    }

}