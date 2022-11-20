package yukams.app.background_locator_2.cellInfo.core.db.local

interface ILocalStorage {

    /**
     * Checks if TAC / LAC endianness in GSM, WCDMA, LTE networks should be flipped
     * no matter what. Applies only for new cell API
     *
     * @see yukams.app.background_locator_2.cellInfo.core.feature.postprocess.SamsungEndiannessPostprocessor
     */
    var locationAreaEndiannessIncorrect: Boolean

    /**
     * True if this device is capable of returning LTE bandwidth for a primary
     * LTE cell directly via getAllCellInfo() method
     */
    var reportsLteBandwidthDirectly: Boolean
}