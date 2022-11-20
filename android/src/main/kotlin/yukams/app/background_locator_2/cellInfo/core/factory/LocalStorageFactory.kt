package yukams.app.background_locator_2.cellInfo.core.factory

import android.content.Context
import yukams.app.background_locator_2.cellInfo.core.db.local.DummyStorage
import yukams.app.background_locator_2.cellInfo.core.db.local.ILocalStorage
import yukams.app.background_locator_2.cellInfo.core.db.local.LocalStorage
import yukams.app.background_locator_2.cellInfo.core.model.NetMonsterConfig

/**
 * Internal factory producing [ILocalStorage]
 */
internal object LocalStorageFactory {

    fun get(context: Context, config: NetMonsterConfig): ILocalStorage = when (config.stateful) {
        true -> LocalStorage.getInstance(context = context)
        false -> DummyStorage
    }

}