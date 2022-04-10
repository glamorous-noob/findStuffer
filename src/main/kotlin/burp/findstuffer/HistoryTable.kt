package burp.findstuffer

import burp.IHttpRequestResponse
import burp.IHttpService
import burp.findstuffer.rowfilters.IRowFilter
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JTable

/**
 * Find stuffer history table
 *
 * @property tableModel
 * @property mainUI
 * @constructor Create empty Find stuffer history table
 */
class HistoryTable(
    private val tableModel: HistoryTableModel,
    private val mainUI: FindStufferUI
) : JTable(tableModel) {
    // represents the selected "#" number
    private var selectedRowNumber: Int? = null

    init {
        tableHeader.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                sortRowsByColumn(columnAtPoint(e.point))
            }
        })
    }

    private fun sortRowsByColumn(column: Int) {
        // Reset column names to default
        for (i in 0 until tableModel.columnCount) {
            tableHeader.columnModel.getColumn(i).headerValue = tableModel.getColumnName(i)
        }
        // Sorting and setting new column name
        tableHeader.columnModel.getColumn(column).headerValue = tableModel.sort(column)
        tableHeader.repaint()

        applyRowSelection()
    }

    /**
     * Updates the UI to select currently selected row if possible, to be used after filtering or sorting modifications
     */
    private fun applyRowSelection() {
        selectedRowNumber?.let {
            val selectedRowIndex = tableModel.rowNumberToIndex(it)
            if (selectedRowIndex == null) {
                selectedRowNumber = null
                clearSelection()
                mainUI.tableSelectionCleared()
                repaint()
                return
            } else {
                setRowSelectionInterval(selectedRowIndex, selectedRowIndex)
            }
            repaint()
        }
    }

    /**
     * Set content
     *
     * @param dataItems
     */
    fun initContent(dataItems: Array<IHttpRequestResponse>) {
        tableModel.initContent(dataItems)
    }

    override fun changeSelection(row: Int, col: Int, toggle: Boolean, extend: Boolean) {
        val newlySelectedRow = tableModel.filteredSortedRows[row]
        // update the "#" number
        selectedRowNumber = newlySelectedRow.number
        // Tell the main UI the selection changed
        mainUI.tableSelectionChanged(newlySelectedRow.request, newlySelectedRow.response)
        super.changeSelection(row, col, toggle, extend)
    }

    /**
     * Update content
     *
     * @param dataItems
     */
    fun resetContent(dataItems: Array<IHttpRequestResponse>) {
        selectedRowNumber = null
        clearSelection()
        tableModel.resetContent(dataItems)
        repaint()
    }

    fun getSelectedHttpService(): IHttpService? {
        selectedRowNumber?.let {
            //if the selected row number does not correspond to any existing row, that means I f***** up somewhere
            // Let's just return null silently and hope for the best. What's the worst that could happen, right? :)
            val index = tableModel.rowNumberToIndex(it) ?: return null
            return tableModel.filteredSortedRows[index].httpService
        }
        return null
    }

    fun useFilter(filter: IRowFilter) {
        tableModel.useNewFilters(filter)
        applyRowSelection()
    }

    fun clearFilters() {
        tableModel.clearFilters()
        applyRowSelection()
    }


}