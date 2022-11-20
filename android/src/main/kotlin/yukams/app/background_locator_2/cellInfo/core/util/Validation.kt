package yukams.app.background_locator_2.cellInfo.core.util

/**
 * Returns null if [requirement] returns true
 */
fun <T> T.nullIf(requirement: (T) -> Boolean) =
    if (requirement.invoke(this)) {
        null
    } else {
        this
    }