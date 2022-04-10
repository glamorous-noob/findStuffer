package burp.findstuffer.rowfilters

import burp.findstuffer.HistoryRowData
import burp.findstuffer.search.TextQueryScope

class TextBothFilter(stringQuery: String) : TextFilter(stringQuery) {
    override val scope = TextQueryScope.REQUEST_AND_RESPONSE

    override fun rowMeetsCriteria(row: HistoryRowData): Boolean {
        var smallData: ByteArray
        var bigData: ByteArray
        if (row.request.size < row.response.size) {
            smallData = row.request
            bigData = row.response
        } else {
            smallData = row.response
            bigData = row.request
        }
        return queryInData(byteQuery, smallData) && queryInData(byteQuery, bigData)
    }

}