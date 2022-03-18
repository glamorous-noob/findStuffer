package burp

import burp.BurpExtender.Companion.callbacks
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JTable

/**
 * Find stuffer history table
 *
 * @property tableData
 * @property mainUI
 * @constructor Create empty Find stuffer history table
 */
class FindStufferHistoryTable (
    private val tableData: FindStufferTableModel,
    private val mainUI: FindStufferUI ) : JTable(tableData) {
    // represents the selected "#" number
    var selectedRowNumber : Int? = null
        private set

    init {
        tableHeader.addMouseListener(object : MouseAdapter(){
            override fun mouseClicked(e: MouseEvent) {
                sortRowsByColumn(columnAtPoint(e.point))
            }
        })
    }

    private fun sortRowsByColumn(column: Int) {
        // Reset column names to default
        for (i in 0 until tableData.columnCount){
            tableHeader.columnModel.getColumn(i).headerValue = tableData.getColumnName(i)
        }
        // Sorting and setting new column name
        tableHeader.columnModel.getColumn(column).headerValue = tableData.sort(column)
        tableHeader.repaint()

        applyRowSelection()
    }

    private fun applyRowSelection(){
        selectedRowNumber?.let {
            val selectedRowIndex = tableData.rowNumberToIndex(it)
            callbacks.issueAlert("selected row number $it & index $selectedRowIndex")
            if(selectedRowIndex==null){
                selectedRowNumber = null
                clearSelection()
                mainUI.tableSelectionCleared()
                repaint()
                return
            } else{
                setRowSelectionInterval(selectedRowIndex, selectedRowIndex)
            }
        }
        repaint()
    }

    /**
     * Set content
     *
     * @param dataItems
     */
    fun initContent(dataItems: Array<IHttpRequestResponse>) {
        tableData.initContent(dataItems)
    }

    override fun changeSelection(row: Int, col: Int, toggle: Boolean, extend: Boolean) {
        // update the "#" number
        selectedRowNumber = tableData.filteredSortedRows[row].number
        // Tell the main UI the selection changed
        mainUI.tableSelectionChanged(tableData.filteredSortedRows[row].data)
        super.changeSelection(row, col, toggle, extend)
    }

    /**
     * Update content
     *
     * @param dataItems
     */
    fun resetContent(dataItems: Array<IHttpRequestResponse>) {
        selectedRowNumber=null
        clearSelection()
        tableData.resetContent(dataItems)
        repaint()
    }

    fun getSelectedHttpService(): IHttpService? {
        selectedRowNumber?.let {
            //if the selected row number does not correspond to any existing row, that means I f***** up somewhere
            // Let's just return null silently and hope for the best. What's the worst that could happen, right? :)
            val index = tableData.rowNumberToIndex(it) ?: return null
            return tableData.filteredSortedRows[index].data.httpService
        }
        return null
    }

    fun useFilter(filter: IFindStufferRowFilter) {
        tableData.useNewFilter(filter)
        applyRowSelection()
    }

    fun clearFilters() {
        tableData.clearFilters()
        applyRowSelection()
    }


}