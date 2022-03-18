package burp

import burp.BurpExtender.Companion.callbacks
import java.awt.*
import javax.swing.*

class FindStufferSearchModal(private val mainUI: FindStufferUI) : JDialog(null, "Let's find stuff", Dialog.ModalityType.APPLICATION_MODAL) {
    //TODO do a real implementation for this. The idea is for the user to be able to dynamically add search fields to
    // the modal via some button
    private var searchFields = arrayListOf<FindStufferSearchField>()

    init {
        defaultCloseOperation = JDialog.DISPOSE_ON_CLOSE
        size = Dimension(500,300)
        // search modal buttons panel
        val buttonsPanel = JPanel(FlowLayout(FlowLayout.RIGHT))
        val okButton = JButton("OK")
        okButton.preferredSize = Dimension(200, 30)
        buttonsPanel.add(okButton)
        okButton.addActionListener{ executeSearch() }
        //search fields
        searchFields.add(FindStufferSearchField())
        // search modal layout
        layout = BorderLayout()
        add(searchFields[0], BorderLayout.PAGE_START)
        add(buttonsPanel, BorderLayout.PAGE_END)
    }

    //TODO probably create a searchQuery class that holds the information needed, and pass it to the main UI
    private fun executeSearch(){
        mainUI.executeSearch(searchFields[0].text, SearchQueryScope.REQUEST)
        isVisible=false
    }

    fun display() {
        isVisible=true
    }

}