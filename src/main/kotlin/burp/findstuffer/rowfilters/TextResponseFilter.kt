package burp.findstuffer.rowfilters

import burp.findstuffer.HistoryRowData
import burp.findstuffer.SearchQueryScope

class TextResponseFilter(stringQuery: String) : TextFilter(stringQuery) {
    override val scope = SearchQueryScope.RESPONSE

    override fun rowMeetsCriteria(row: HistoryRowData): Boolean {
        return queryInData(byteQuery, row.data.response)
    }

}
