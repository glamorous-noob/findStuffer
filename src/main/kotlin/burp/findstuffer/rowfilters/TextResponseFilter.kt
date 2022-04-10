package burp.findstuffer.rowfilters

import burp.findstuffer.HistoryRowData
import burp.findstuffer.search.TextQueryScope

class TextResponseFilter(stringQuery: String, caseSensitive: Boolean) : TextFilter(stringQuery, caseSensitive) {
    override val scope = TextQueryScope.RESPONSE

    override fun rowMeetsCriteria(row: HistoryRowData): Boolean {
        return queryInData(byteQuery, row.response)
    }

}
