package yukams.app.background_locator_2.cellInfo.core.model.connection

/**
 * Secondary cell for data communication (aggregated cell).
 *
 */
data class SecondaryConnection(
    /**
     * true = Guess made by NetMonster Core based on other indicators from RIL.
     *
     * false = Reported directly from device's RIL.
     */
    val isGuess: Boolean
) : IConnection