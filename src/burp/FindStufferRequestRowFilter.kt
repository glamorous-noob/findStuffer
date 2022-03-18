package burp

import burp.BurpExtender.Companion.callbacks
import kotlin.math.min

class FindStufferRequestRowFilter(private val stringQuery : String) : IFindStufferRowFilter{
    private val byteQuery by lazy { callbacks.helpers.stringToBytes(stringQuery) }

    //TODO could be optimized by remembering potential comparison start indexes, to avoid comparing the same bytes many times
    override fun rowMeetsCriteria(row : FindStufferRowData): Boolean {
        val request = row.data.request
        return callbacks.helpers.indexOf(request, byteQuery, false, 0, request.size-1)!=-1
    }

    override fun toString(): String {
        return "Searching for $stringQuery in request."
    }

}