package burp.findstuffer

class TextQuery(val text : String, val scope : TextQueryScope, val negativeQuery: Boolean) {
    fun isEmpty() : Boolean {
        return text.isEmpty()
    }
}