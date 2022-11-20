package yukams.app.background_locator_2.cellInfo.core.model.signal

interface ISignal {

    /**
     * Main indicator for signal measurement. Range of this value depends on current network type.
     * In most cases you'll get RSSI value (if present)
     */
    val dbm: Int?

}