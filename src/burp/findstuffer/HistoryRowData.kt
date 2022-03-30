package burp.findstuffer

import burp.IHttpRequestResponse
import burp.IHttpRequestResponsePersisted
import burp.IRequestInfo
import burp.IResponseInfo
import burp.BurpExtender.Companion.callbacks
import java.net.URL

// For now, keeping the IHttpRequestResponse inside the data object since I found no reliable way to reference
// this data object later from callbacks.proxyHistory otherwise.
class HistoryRowData(val number : Int, initData : IHttpRequestResponse) {

    val data: IHttpRequestResponsePersisted = callbacks.saveBuffersToTempFiles(initData)
    private val requestInfo : IRequestInfo by lazy { callbacks.helpers.analyzeRequest(initData) }
    private val responseInfo : IResponseInfo? by lazy {
        if(initData.response==null) null else callbacks.helpers.analyzeResponse(initData.response)
    }

    val protocol: String = initData.httpService.protocol
    val method: String by lazy { requestInfo.method }
    val url: URL by lazy { requestInfo.url }
    val statusCode : Short? by lazy { responseInfo?.statusCode }
    val responseLength = initData.response?.size
    val mimeType : String? by lazy { responseInfo?.inferredMimeType }
}