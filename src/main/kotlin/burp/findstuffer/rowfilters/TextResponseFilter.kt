package burp.findstuffer.rowfilters

import burp.findstuffer.HistoryRowData
import burp.findstuffer.TextQueryScope

class TextResponseFilter(stringQuery: String) : TextFilter(stringQuery) {
    override val scope = TextQueryScope.RESPONSE

    override fun rowMeetsCriteria(row: HistoryRowData): Boolean {
        return queryInData(byteQuery, row.data.response)
    }

}
