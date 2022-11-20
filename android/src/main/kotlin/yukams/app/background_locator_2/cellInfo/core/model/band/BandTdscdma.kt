package yukams.app.background_locator_2.cellInfo.core.model.band

import android.os.Build
import yukams.app.background_locator_2.cellInfo.core.db.BandTableTdscdma
import yukams.app.background_locator_2.cellInfo.core.model.annotation.SinceSdk

@SinceSdk(Build.VERSION_CODES.Q)
data class BandTdscdma(
    val downlinkUarfcn: Int,
    override val number: Int?,
    override val name: String?
) : IBand {

    override val channelNumber: Int = downlinkUarfcn

    companion object {

        /**
         * Minimal UARFCN
         */
        const val DOWNLINK_UARFCN_MIN = 0

        /**
         * UARFCN is 14-bit number. This value represents 2^14 - 1
         */
        const val DOWNLINK_UARFCN_MAX = 16_383L

        internal val DOWNLINK_UARFCN_RANGE = DOWNLINK_UARFCN_MIN..DOWNLINK_UARFCN_MAX
    }
}