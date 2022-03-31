package burp.findstuffer.rowfilters

import burp.findstuffer.search.TextQuery
import burp.findstuffer.search.TextQueryScope

class RowFilterFactory {

    private fun getTextFilter(textQuery: TextQuery) : IRowFilter {
        val filter =  when(textQuery.scope){
            TextQueryScope.REQUEST -> TextRequestFilter(textQuery.text)
            TextQueryScope.RESPONSE -> TextResponseFilter(textQuery.text)
            TextQueryScope.REQUEST_AND_RESPONSE -> TextBothFilter(textQuery.text)
            TextQueryScope.REQUEST_OR_RESPONSE -> TextAnyFilter(textQuery.text)
        }
        return if(textQuery.negativeQuery) NegativeFilter(filter)
        else filter
    }

    fun getAggregatedTextFilters(textQueries: List<TextQuery>, aggregationType: RowFilterAggregationType): IRowFilter {
        val individualFilters = textQueries.filterNot { it.isEmpty() }.map { getTextFilter(it) }
        return when(aggregationType) {
            RowFilterAggregationType.AND -> RowFilterAndAggregator(individualFilters)
            RowFilterAggregationType.OR -> RowFilterOrAggregator(individualFilters)
        }
    }
}