package yukams.app.background_locator_2.cellInfo.core.db.local

/**
 * Represents local storage implementation whose values are immutable constants
 */
object DummyStorage : ILocalStorage {

    override var locationAreaEndiannessIncorrect: Boolean
        get() = false
        set(_) {}

    override var reportsLteBandwidthDirectly: Boolean
        get() = false
        set(_) {}
}