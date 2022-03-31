package burp.findstuffer.rowfilters

import burp.findstuffer.HistoryRowData
import burp.findstuffer.search.TextQueryScope

class TextAnyFilter(stringQuery : String)  : TextFilter(stringQuery) {
    override val scope = TextQueryScope.REQUEST_OR_RESPONSE

    override fun rowMeetsCriteria(row: HistoryRowData): Boolean {
        val smallData: ByteArray
        val bigData: ByteArray
        if(row.data.request.size < row.data.response.size){
            smallData = row.data.request
            bigData = row.data.response
        }
        else{
            smallData = row.data.response
            bigData = row.data.request
        }
        return queryInData(byteQuery, smallData) || queryInData(byteQuery, bigData)
    }
}
