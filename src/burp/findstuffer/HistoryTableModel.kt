package burp.findstuffer

import burp.IHttpRequestResponse
import burp.findstuffer.rowfilters.IRowFilter
import java.util.function.Function
import javax.swing.table.AbstractTableModel

/**
 * Implements the table model used by the history logs table
 * Also holds all the data needed for that
 *
 * @constructor Create empty Find stuffer table model
 */
//TODO I've implemented way too much functionality that seems to be already present in Java Swing's librairies
// This code should be simplified by using said functionality (sorting - filtering - etc.)
// In my defense, I did this because the native table filtering was really sucky, laggy and buggy
class HistoryTableModel : AbstractTableModel() {
    // Defining column names in order and what they correspond to
    // This solution was used instead of a map because it makes it easier to deal with JTable's column indexes
    private val columns = arrayOf(
        // This "#" will not correspond to the one in the proxy history tab if at least one line has been deleted
        FindStufferColumn("#" ,  { row -> row.number }, compareBy { it.number } ),
        FindStufferColumn("Protocol" ,  { row -> row.protocol }, compareBy<HistoryRowData> { it.protocol }.thenBy { it.number }),
        FindStufferColumn("Host" , { row -> row.url.host }, compareBy<HistoryRowData> { it.url.host}.thenBy { it.number }),
        FindStufferColumn("URL Path" , { row -> row.url.path }, compareBy<HistoryRowData> { it.url.path}.thenBy { it.number }),
        FindStufferColumn("Method" , { row -> row.method }, compareBy<HistoryRowData> { it.method}.thenBy { it.number }),
        FindStufferColumn("Status" , { row -> row.statusCode ?: "" }, compareBy<HistoryRowData> { it.statusCode}.thenBy { it.number }),
        FindStufferColumn("Response Length" , { row -> row.responseLength ?: "" }, compareBy<HistoryRowData> { it.responseLength}.thenBy { it.number }),
        FindStufferColumn("MIME Type" , { row -> row.mimeType ?: ""  }, compareBy<HistoryRowData> { it.mimeType}.thenBy { it.number })
    )

    // The non-sorted non-filtered version, the underlying list
    var allRows : List<HistoryRowData> = arrayListOf()
    // Used to interface with the table UI through the overridden methods
    // Must contain shallow copies only to the objects in allRows -- no object creation
    var filteredSortedRows : MutableList<HistoryRowData> = arrayListOf()
    // Maps the displayed "#" number to the actual row index. Used to maintain selected row after sorting
    private var rowNumberToIndexMap : MutableMap<Int, Int> = hashMapOf()
    private var sortColumn : Int? = null
    //TODO allow for the application of more than one filter at a time either by making this a list or by
    // creating an IFindStufferRowFilter that aggregates many of them
    private var appliedFilter : IRowFilter? = null

    /**
     *
     *
     * @param data the array of proxy history items as returned by callbacks.proxyHistory
     */
    fun initContent(data : Array<IHttpRequestResponse>){
        allRows = data.mapIndexed { index, element -> HistoryRowData(index+1, element) }
        filteredSortedRows = allRows.toMutableList()
        rowNumberToIndexMap = hashMapOf()
        updateSortedIndexes()
        fireTableDataChanged()
    }

    /**
     *
     * @param number The number displayed for the row in the "#" column, which never changes
     * @return the actual index of the row, which changes when the rows are sorted
     */
    fun rowNumberToIndex(number: Int) : Int? {
        return rowNumberToIndexMap[number]
    }

    /**
     * Update the FindStuffer table rows based on the current state of the HTTP proxy history.
     *
     * Very primitive implementation, but can't do a lot better at the moment because of Burp's API.
     * dataItems comes from callbacks.proxyHistory, it is just an array with no specific identifier for requests.
     * The values of the "#" column in the proxy history tab are not included in the array.
     * They can not be reliably deduced based the element order in the array (because of potential deleted rows).
     * Therefore, trying to identify only the new rows since last update is so not straightforward that it's really not
     * worth it.
     * @param data the array returned by callbacks.proxyHistory
     */
    fun resetContent(data: Array<IHttpRequestResponse>) {
        allRows = data.mapIndexed { index, element -> HistoryRowData(index+1, element) }
        appliedFilter?.let {
            useNewFilter(it) // reapply current filter if not null, sorting is included in useNewFilter
        } ?: run {
            filteredSortedRows = allRows.toMutableList()
            reapplyCurrentSorting() // sort if necessary
            updateSortedIndexes()
            fireTableDataChanged()
        }
    }


    private fun updateSortedIndexes() {
        rowNumberToIndexMap = hashMapOf()
        filteredSortedRows.forEachIndexed { ind, el ->
            rowNumberToIndexMap[el.number] = ind
        }
    }

    //TODO maybe rename this to sort() and make it an override of the other existing sort method ?
    private fun reapplyCurrentSorting(){
        sortColumn?.let {
            doSorting(it, columns[it].sortState== SortStateEnum.DESCENDING)
        }
    }

    private fun doSorting(column: Int, reverse: Boolean) {
        filteredSortedRows.sortWith(getColumnComparator(column))
        if(reverse) filteredSortedRows.reverse()
        columns.forEach { it.sortState = SortStateEnum.NONE }
        columns[column].sortState = if(reverse) SortStateEnum.DESCENDING else SortStateEnum.ASCENDING
    }

    /**
     * Sort the table's rows
     *
     * @param column the column's index on which the sorting is done
     * @return The new temporary title of the column, to be applied by the table
     */
    fun sort(column: Int) : String {
        sortColumn = column
        // If the rows are already sorted in ascending order, we should do descending order (reverse=true)
        val reverse = columns[column].sortState== SortStateEnum.ASCENDING
        doSorting(column, reverse)
        updateSortedIndexes()
        fireTableDataChanged()
        return columns[column].name + (if (reverse) " (desc.)" else " (asc.)")
    }

    override fun getColumnName(column: Int): String {
        if(column >= 0 && column < columns.size) return columns[column].name
        return ""
    }

    private fun getColumnComparator(column : Int) : Comparator<HistoryRowData> {
        return columns[column].comparator
    }

    override fun getRowCount(): Int {
        return filteredSortedRows.size
    }

    override fun getColumnCount(): Int {
        return columns.size
    }

    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any {
        val row = filteredSortedRows[rowIndex]
        return columns[columnIndex].cellDisplayedValGetter.apply(row)
    }

    // Simple implementation
    //TODO improve this. Or create a useAdditionalFilter function ?
    //TODO is it really the best choice to systematically couple filtering with sorting here ?
    fun useNewFilter(filter: IRowFilter) {
        filteredSortedRows = allRows.filter { row  -> filter.rowMeetsCriteria(row) }.toMutableList()
        appliedFilter = filter
        reapplyCurrentSorting()
        updateSortedIndexes()
        fireTableDataChanged()
    }

    fun clearFilters() {
        filteredSortedRows = allRows.toMutableList()
        appliedFilter = null
        reapplyCurrentSorting()
        updateSortedIndexes()
        fireTableDataChanged()
    }

    private class FindStufferColumn(val name : String, val cellDisplayedValGetter : Function<HistoryRowData, Any>, val comparator: Comparator<HistoryRowData>){
        var sortState : SortStateEnum = SortStateEnum.NONE
    }

}