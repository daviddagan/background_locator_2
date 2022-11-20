package yukams.app.background_locator_2.cellInfo.core.feature.postprocess

import yukams.app.background_locator_2.cellInfo.core.model.cell.ICell

/**
 * Postprocessor allowing to change, intercept & change data
 */
interface ICellPostprocessor {

    /**
     * Postprocessing method that allows data modification within the list
     */
    fun postprocess(list: List<ICell>) : List<ICell>

}