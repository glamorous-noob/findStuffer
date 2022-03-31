package burp.findstuffer.rowfilters

import burp.findstuffer.search.TextQuery
import burp.findstuffer.search.TextQueryScope

class RowFilterFactory {

    fun getTextFilter(textQuery: TextQuery) : IRowFilter {
        val filter =  when(textQuery.scope){
            TextQueryScope.REQUEST -> TextRequestFilter(textQuery.text)
            TextQueryScope.RESPONSE -> TextResponseFilter(textQuery.text)
            TextQueryScope.REQUEST_AND_RESPONSE -> TextBothFilter(textQuery.text)
            TextQueryScope.REQUEST_OR_RESPONSE -> TextAnyFilter(textQuery.text)
        }
        return if(textQuery.negativeQuery) NegativeFilter(filter)
        else filter
    }

    fun getAndAggregatedTextFilters(textQueries : Collection<TextQuery>) : IRowFilter {
        return RowFilterAndAggregator(
            textQueries.filterNot { it.isEmpty() }
                .map { getTextFilter(it) }
        )
    }
}