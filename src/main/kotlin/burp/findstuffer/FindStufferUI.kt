package burp.findstuffer

import burp.BurpExtender.Companion.callbacks
import burp.IHttpRequestResponse
import burp.IHttpService
import burp.IMessageEditorController
import burp.ITab
import burp.findstuffer.rowfilters.RowFilterAggregationType
import burp.findstuffer.rowfilters.RowFilterFactory
import burp.findstuffer.search.SearchModal
import burp.findstuffer.search.TextQuery
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Component
import java.awt.Dimension
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.*


/**
 * Manages the top-level UI aspects of the extension and orchestrates communication between different
 * components.
 *
 * @constructor
 *///TODO Start implementing filtering https://stackoverflow.com/questions/944006/how-can-i-hide-make-invisible-row-in-jtable
// Build the UI for the search dialog
// Build the refresh history button
class FindStufferUI : ITab, IMessageEditorController {
    private val NO_APPLIED_FILTERS: String = "No applied filters."

    // TODO revisit these components and experiment with other swing classes that might be more relevant than JPanel
    // search bar and history reset button panel
    private val mainJPanel = JPanel()
    private val toolBar = JPanel()
    private val searchBar = JTextField()
    private val historyResetButton = JButton("Repopulate")

    // main split pane
    private val historyAndEditorsSplitPane = JSplitPane(JSplitPane.VERTICAL_SPLIT)

    // table of log entries
    private val historyTable = HistoryTable(HistoryTableModel(), this)
    private val historyScrollPane = JScrollPane(historyTable)

    // tabs with request/response viewers
    private val requestAndResponseEditors = JSplitPane(JSplitPane.HORIZONTAL_SPLIT)
    private val requestViewer = callbacks.createMessageEditor(this, false)
    private val responseViewer = callbacks.createMessageEditor(this, false)

    // Search modal
    private val searchModal = SearchModal(this)

    // init components
    init {
        mainJPanel.layout = BorderLayout()

        // Search bar + history reset button
        mainJPanel.add(toolBar, BorderLayout.PAGE_START)
        toolBar.layout = BorderLayout()
        searchBar.text = NO_APPLIED_FILTERS
        searchBar.isEditable = false
        searchBar.border = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(4, 3, 0, 0)
        )
        toolBar.add(searchBar, BorderLayout.CENTER)
        historyResetButton.preferredSize = Dimension(200, 30)
        historyResetButton.toolTipText =
            "Repopulates table entries according to the current state of the proxy history."
        toolBar.add(historyResetButton, BorderLayout.LINE_END)

        // The history logs + the request and response editors
        mainJPanel.add(historyAndEditorsSplitPane, BorderLayout.CENTER)
        historyAndEditorsSplitPane.leftComponent = historyScrollPane
        historyAndEditorsSplitPane.rightComponent = requestAndResponseEditors
        requestAndResponseEditors.leftComponent = requestViewer.component
        requestAndResponseEditors.rightComponent = responseViewer.component
        requestAndResponseEditors.resizeWeight = 0.5

        // Customize to have burp looks
        callbacks.customizeUiComponent(mainJPanel)
        callbacks.customizeUiComponent(toolBar)
        callbacks.customizeUiComponent(searchBar)
        callbacks.customizeUiComponent(historyResetButton)
        callbacks.customizeUiComponent(historyAndEditorsSplitPane)
        callbacks.customizeUiComponent(historyTable)
        callbacks.customizeUiComponent(historyScrollPane)
        callbacks.customizeUiComponent(requestAndResponseEditors)
        callbacks.customizeUiComponent(searchModal)
        //TODO add the rest of the components

        // setup listeners on ui elements
        historyResetButton.addActionListener {
            emptyRequestResponseViewers()
            historyTable.resetContent(callbacks.proxyHistory)
        }
        searchBar.addMouseListener(object : MouseListener {
            override fun mouseClicked(e: MouseEvent?) {
                searchModal.display()
            }

            override fun mousePressed(e: MouseEvent?) {}
            override fun mouseReleased(e: MouseEvent?) {}
            override fun mouseEntered(e: MouseEvent?) {}
            override fun mouseExited(e: MouseEvent?) {}
        })
    }

    override fun getTabCaption(): String {
        return "FindStuffer"
    }

    override fun getUiComponent(): Component {
        return mainJPanel
    }

    /**
     * Table selection changed
     *
     * @param data
     */
    fun tableSelectionChanged(data: IHttpRequestResponse) {
        requestViewer.setMessage(data.request, true)
        responseViewer.setMessage(data.response, false)
    }

    //TODO add more stuff here if needed
    fun tableSelectionCleared() {
        emptyRequestResponseViewers()
    }

    private fun emptyRequestResponseViewers() {
        requestViewer.setMessage(byteArrayOf(), true)
        responseViewer.setMessage(byteArrayOf(), true)
    }

    /**
     * Set history table content
     *
     * @param dataItems
     */
    fun setHistoryTableContent(dataItems: Array<IHttpRequestResponse>) {
        historyTable.initContent(dataItems)
    }

    override fun getHttpService(): IHttpService? {
        return historyTable.getSelectedHttpService()
    }

    override fun getRequest(): ByteArray {
        return requestViewer.message
    }

    override fun getResponse(): ByteArray {
        return responseViewer.message
    }

    //TODO !!!
    fun executeSearch(queries: List<TextQuery>, aggregationType: RowFilterAggregationType) {
        if (queries.all { it.isEmpty() }) {
            historyTable.clearFilters()
            searchBar.text = NO_APPLIED_FILTERS
        } else {
            val filter = RowFilterFactory().getAggregatedTextFilters(queries, aggregationType)
            historyTable.useFilter(filter)
            searchBar.text = "Searching for $filter"
        }
    }

}