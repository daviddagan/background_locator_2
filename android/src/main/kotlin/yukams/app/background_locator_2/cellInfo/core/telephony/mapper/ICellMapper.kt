package yukams.app.background_locator_2.cellInfo.core.telephony.mapper

import yukams.app.background_locator_2.cellInfo.core.model.cell.ICell

/**
 * Mapper calls transforms AOSP's representation of model into ours
 */
interface ICellMapper<T> {

    /**
     * Map method
     * @return list of cells that are valid or empty list if nothing is correct
     */
    fun map(model: T) : List<ICell>

}