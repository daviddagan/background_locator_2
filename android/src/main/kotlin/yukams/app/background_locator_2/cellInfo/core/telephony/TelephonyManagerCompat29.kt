package yukams.app.background_locator_2.cellInfo.core.telephony

import android.Manifest
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager
import androidx.annotation.RequiresPermission
import androidx.annotation.WorkerThread
import yukams.app.background_locator_2.cellInfo.core.callback.CellCallbackError
import yukams.app.background_locator_2.cellInfo.core.callback.CellCallbackSuccess
import yukams.app.background_locator_2.cellInfo.core.model.cell.ICell
import yukams.app.background_locator_2.cellInfo.core.model.model.CellError
import yukams.app.background_locator_2.cellInfo.core.telephony.mapper.CellInfoCallbackMapper
import yukams.app.background_locator_2.cellInfo.core.util.DirectExecutor

/**
 * Modifies some functionalities of [TelephonyManager] and unifies access to
 * methods across all platform versions.
 */
@TargetApi(Build.VERSION_CODES.Q)
internal open class TelephonyManagerCompat29(
    context: Context,
    subId: Int = Integer.MAX_VALUE
) : TelephonyManagerCompat17(context, subId) {


    @WorkerThread
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun getAllCellInfo(
        onSuccess: CellCallbackSuccess,
        onError: CellCallbackError?
    ) {
        try {
            telephony.requestCellInfoUpdate(DirectExecutor(), CellInfoCallbackMapper(
                success = { cells -> onSuccess.invoke(cellInfoMapper.map(cells)) },
                error = { errorCode ->
                    if (onError != null) {
                        onError.invoke(errorCode)
                    } else {
                        onSuccess.invoke(cellInfoMapper.map(telephony.allCellInfo))
                    }
                }
            ))
        } catch (e: IllegalStateException) {
            onError?.invoke(CellError.UNKNOWN)
        }
    }

    @RequiresPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
    override fun getNeighboringCellInfo(): List<ICell> = emptyList()


}