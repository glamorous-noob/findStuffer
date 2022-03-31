package burp.findstuffer.search

enum class TextQueryScope {
    REQUEST, RESPONSE, REQUEST_OR_RESPONSE, REQUEST_AND_RESPONSE;

    companion object {
        val SCOPES = values()
    }

    override fun toString(): String {
        return super.toString().replace('_', ' ').lowercase()
    }

}