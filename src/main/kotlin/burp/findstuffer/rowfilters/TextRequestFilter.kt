package burp.findstuffer.rowfilters

import burp.findstuffer.HistoryRowData
import burp.findstuffer.search.TextQueryScope

class TextRequestFilter(stringQuery: String) : TextFilter(stringQuery) {
    override val scope = TextQueryScope.REQUEST

    override fun rowMeetsCriteria(row: HistoryRowData): Boolean {
        return queryInData(byteQuery, row.request)
    }
}