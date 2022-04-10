package burp.findstuffer.rowfilters

import burp.findstuffer.HistoryRowData

class RowFilterOrAggregator(private val filters: Collection<IRowFilter>) : IRowFilter {

    override fun rowMeetsCriteria(row: HistoryRowData): Boolean {
        return filters.any {
            it.rowMeetsCriteria(row)
        }
    }

    override fun toString(): String {
        return filters.joinToString(" ∨ ")
    }
}