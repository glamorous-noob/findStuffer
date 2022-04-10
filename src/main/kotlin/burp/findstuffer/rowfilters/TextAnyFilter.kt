package burp.findstuffer.rowfilters

import burp.findstuffer.HistoryRowData
import burp.findstuffer.search.TextQueryScope

class TextAnyFilter(stringQuery: String) : TextFilter(stringQuery) {
    override val scope = TextQueryScope.REQUEST_OR_RESPONSE

    override fun rowMeetsCriteria(row: HistoryRowData): Boolean {
        val smallData: ByteArray
        val bigData: ByteArray
        if (row.request.size < row.response.size) {
            smallData = row.request
            bigData = row.response
        } else {
            smallData = row.response
            bigData = row.request
        }
        return queryInData(byteQuery, smallData) || queryInData(byteQuery, bigData)
    }
}
