package yukams.app.background_locator_2.cellInfo.core.telephony.mapper.cell

import android.annotation.TargetApi
import android.os.Build
import android.telephony.CellInfo
import yukams.app.background_locator_2.cellInfo.core.model.connection.IConnection
import yukams.app.background_locator_2.cellInfo.core.model.connection.NoneConnection
import yukams.app.background_locator_2.cellInfo.core.model.connection.PrimaryConnection
import yukams.app.background_locator_2.cellInfo.core.model.connection.SecondaryConnection

/**
 * [CellInfo] -> [IConnection]
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
internal fun CellInfo.mapConnection(): IConnection =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        when(cellConnectionStatus) {
            CellInfo.CONNECTION_PRIMARY_SERVING ->
                PrimaryConnection()
            CellInfo.CONNECTION_SECONDARY_SERVING ->
                SecondaryConnection(isGuess = false)
            else ->
                // Xiaomi Mi A1 returns CellInfo.CONNECTION_NONE & isRegistered = true
                if (isRegistered) {
                    PrimaryConnection()
                } else {
                    NoneConnection()
                }
        }
    } else {
        if (isRegistered) {
            PrimaryConnection()
        } else {
            NoneConnection()
        }
    }