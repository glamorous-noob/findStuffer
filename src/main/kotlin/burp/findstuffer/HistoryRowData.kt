package burp.findstuffer

import burp.*
import burp.BurpExtender.Companion.callbacks
import java.net.URL

// For now, keeping the IHttpRequestResponse inside the data object since I found no reliable way to reference
// this data object later from callbacks.proxyHistory otherwise.
class HistoryRowData(val number: Int, initData: IHttpRequestResponse) {


    // Keeping this as cached version so it doesn't use up memory
    private val data: IHttpRequestResponsePersisted = callbacks.saveBuffersToTempFiles(initData)

    // Lazy wrappers around the request and response arrays to make them null-safe
    val request by lazy { data.request ?: byteArrayOf() }
    val response by lazy { data.response ?: byteArrayOf() }
    private val requestInfo: IRequestInfo by lazy { callbacks.helpers.analyzeRequest(data) }
    private val responseInfo: IResponseInfo? by lazy {
        if (initData.response == null) null else callbacks.helpers.analyzeResponse(data.response)
    }

    val method: String by lazy { requestInfo.method }
    val url: URL by lazy { requestInfo.url }
    val statusCode: Short? by lazy { responseInfo?.statusCode }
    val mimeType: String? by lazy { responseInfo?.inferredMimeType }
    val httpService: IHttpService by lazy { data.httpService }
}