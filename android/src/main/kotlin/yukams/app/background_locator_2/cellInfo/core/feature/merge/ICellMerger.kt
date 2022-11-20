package yukams.app.background_locator_2.cellInfo.core.feature.merge

import yukams.app.background_locator_2.cellInfo.core.model.cell.ICell
import yukams.app.background_locator_2.cellInfo.core.telephony.ITelephonyManagerCompat

/**
 * Merges two lists of [ICell] into single one without duplicities.
 */
interface ICellMerger {

    /**
     * Performs merge of two lists. Returned list's size
     * is in range from min([oldApi].size, [newApi].size) to max([oldApi].size, [newApi].size),
     * both boundaries are inclusive.
     *
     * @param oldApi data from [ITelephonyManagerCompat.getNeighbouringCells] and / or [ITelephonyManagerCompat.getCellLocation]
     * @param newApi data from [ITelephonyManagerCompat.getAllCellInfo]
     * @param displayOn true is any display of current device is on
     */
    fun merge(oldApi: List<ICell>, newApi: List<ICell>, displayOn: Boolean) : List<ICell>

}