package yukams.app.background_locator_2.cellInfo.core.telephony.network

import yukams.app.background_locator_2.cellInfo.core.model.Network
import yukams.app.background_locator_2.cellInfo.core.telephony.ITelephonyManagerCompat

/**
 * Interface that allows us obtain [Network].
 */
interface INetworkGetter {

    /**
     * Fetches network from using [telephony]
     * @return network or null
     */
    fun getNetwork(telephony: ITelephonyManagerCompat): Network?
}