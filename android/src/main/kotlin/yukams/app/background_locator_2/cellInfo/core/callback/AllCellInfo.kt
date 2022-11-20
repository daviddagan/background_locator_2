package yukams.app.background_locator_2.cellInfo.core.callback

import yukams.app.background_locator_2.cellInfo.core.model.cell.ICell
import yukams.app.background_locator_2.cellInfo.core.model.model.CellError

typealias CellCallbackSuccess = (cells: List<ICell>) -> Unit
typealias CellCallbackError = (error: CellError) -> Unit
