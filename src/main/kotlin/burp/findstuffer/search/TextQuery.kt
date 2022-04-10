package burp.findstuffer.search

class TextQuery(val text: String, val scope: TextQueryScope, val negativeQuery: Boolean) {
    fun isEmpty(): Boolean {
        return text.isEmpty()
    }

    constructor(query: TextQuery) : this(query.text, query.scope, query.negativeQuery)
}