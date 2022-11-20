package yukams.app.background_locator_2.cellInfo.core.feature.detect

import yukams.app.background_locator_2.cellInfo.core.INetMonster
import yukams.app.background_locator_2.cellInfo.core.db.model.NetworkType
import yukams.app.background_locator_2.cellInfo.core.telephony.ITelephonyManagerCompat

/**
 * Class that is able to detect current network type
 */
interface INetworkDetector {

    /**
     * Performs search and detects current network type.
     * Returns null if search was not successful or not possible at this moment.
     */
    fun detect(netmonster: INetMonster, telephony: ITelephonyManagerCompat) : NetworkType?

}