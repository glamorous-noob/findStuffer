package burp.findstuffer.rowfilters

import burp.findstuffer.HistoryRowData
import burp.findstuffer.SearchQueryScope

class TextRequestFilter(stringQuery : String) : TextFilter(stringQuery) {
    override val scope = SearchQueryScope.REQUEST

    override fun rowMeetsCriteria(row: HistoryRowData): Boolean {
        return queryInData(byteQuery, row.data.request)
    }
}