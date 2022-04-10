package burp.findstuffer.rowfilters

import burp.findstuffer.HistoryRowData

interface IRowFilter {
    fun rowMeetsCriteria(row: HistoryRowData): Boolean
}
