package yukams.app.background_locator_2.cellInfo.core.feature.postprocess

import yukams.app.background_locator_2.cellInfo.core.model.cell.CellCdma
import yukams.app.background_locator_2.cellInfo.core.model.cell.ICell
import yukams.app.background_locator_2.cellInfo.core.model.connection.PrimaryConnection

/**
 * CDMA cells do not have PLMN (MCC, MNC) by design. However when device is also connected
 * to other network (in most cases it's LTE) then CDMA and LTE cells are both listed as serving
 * under one subscription id.
 *
 * This class grabs PLMN from other cell and assigns it to CDMA cell if it's not already assigned
 */
class CdmaPlmnPostprocessor : ICellPostprocessor {

    override fun postprocess(list: List<ICell>): List<ICell> {
        val subToNetwork =
            list.groupBy { it.subscriptionId }
                .mapValues { (_, cells) ->
                    cells
                        .mapNotNull { cell ->
                            cell.network.takeIf { cell.connectionStatus is PrimaryConnection }
                        }
                        .distinct()
                        .let { networks ->
                            if (networks.size == 1) {
                                networks[0]
                            } else {
                                null
                            }
                        }
                }

        return list.map {
            if (it is CellCdma && it.network == null) {
                it.copy(network = subToNetwork[it.subscriptionId])
            } else {
                it
            }
        }
    }

}