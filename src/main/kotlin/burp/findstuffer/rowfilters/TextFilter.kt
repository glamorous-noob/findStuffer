package burp.findstuffer.rowfilters

import burp.BurpExtender.Companion.callbacks
import burp.findstuffer.search.TextQueryScope

open abstract class TextFilter(private val stringQuery: String) : IRowFilter {
    val byteQuery: ByteArray by lazy { callbacks.helpers.stringToBytes(stringQuery) }
    abstract val scope: TextQueryScope

    fun queryInData(query: ByteArray, data: ByteArray): Boolean {
        return callbacks.helpers.indexOf(data, query, false, 0, data.size) != -1
    }

    override fun toString(): String {
        return "${scope.toString().uppercase()}→$stringQuery"
    }
}