package burp.findstuffer

enum class SearchQueryScope {
    REQUEST, RESPONSE, REQUEST_OR_RESPONSE, REQUEST_AND_RESPONSE;

    override fun toString(): String {
        return super.toString().replace('_', ' ').lowercase()
    }
}