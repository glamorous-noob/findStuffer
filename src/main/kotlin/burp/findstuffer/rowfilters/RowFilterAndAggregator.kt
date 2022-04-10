package burp.findstuffer.rowfilters

import burp.findstuffer.HistoryRowData

class RowFilterAndAggregator(private val filters: Collection<IRowFilter>) : IRowFilter {

    override fun rowMeetsCriteria(row: HistoryRowData): Boolean {
        return filters.all {
            it.rowMeetsCriteria(row)
        }
    }

    override fun toString(): String {
        return filters.joinToString(" ∧ ")
    }
}