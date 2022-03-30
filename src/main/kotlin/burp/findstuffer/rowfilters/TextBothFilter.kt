package burp.findstuffer.rowfilters

import burp.findstuffer.HistoryRowData
import burp.findstuffer.TextQueryScope

class TextBothFilter(stringQuery : String)  : TextFilter(stringQuery) {
    override val scope = TextQueryScope.REQUEST_AND_RESPONSE

    override fun rowMeetsCriteria(row: HistoryRowData): Boolean {
        var smallData: ByteArray
        var bigData: ByteArray
        if(row.data.request.size < row.data.response.size){
            smallData = row.data.request
            bigData = row.data.response
        }
        else{
            smallData = row.data.response
            bigData = row.data.request
        }
        return queryInData(byteQuery, smallData) && queryInData(byteQuery, bigData)
    }

}