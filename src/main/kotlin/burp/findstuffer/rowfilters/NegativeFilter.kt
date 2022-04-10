package burp.findstuffer.rowfilters

import burp.findstuffer.HistoryRowData

class NegativeFilter(private val filter: TextFilter) : IRowFilter {
    override fun rowMeetsCriteria(row: HistoryRowData): Boolean {
        return !filter.rowMeetsCriteria(row)
    }

    override fun toString(): String {
        return "¬($filter)"
    }

}
