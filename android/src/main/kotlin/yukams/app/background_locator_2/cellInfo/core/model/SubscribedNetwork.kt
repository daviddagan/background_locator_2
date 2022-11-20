package yukams.app.background_locator_2.cellInfo.core.model

/**
 * Composite model for SubscriptionManager
 */
data class SubscribedNetwork(
    val simSlotIndex: Int,
    val subscriptionId: Int,
    val network: Network?
)