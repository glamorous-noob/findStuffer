package burp.findstuffer

import burp.BurpExtender.Companion.callbacks
import java.awt.*
import javax.swing.ImageIcon
import javax.swing.JButton
import javax.swing.JDialog
import javax.swing.JPanel

class SearchModal(private val mainUI: FindStufferUI) :
    JDialog(null, "Let's find stuff", ModalityType.APPLICATION_MODAL) {
    //TODO do a real implementation for this. The idea is for the user to be able to dynamically add search fields to
    // the modal via some button
    private var searchFields = arrayListOf<TextQueryField>()
    var nextSearchFieldContainer: Container

    init {
        defaultCloseOperation = DISPOSE_ON_CLOSE
        size = Dimension(500, 300)
        // OK button
        val okButton = JButton("OK")
        okButton.preferredSize = Dimension(100, 30)
        okButton.addActionListener { executeSearch() }
        // Add search field button
        val addSearchFieldButton = JButton(
            ImageIcon(
                ImageIcon(
                    SearchModal::class.java.classLoader.getResource("addButtonIcon.png")
                ).image.getScaledInstance(20, 20, Image.SCALE_SMOOTH)
            )
        )
        addSearchFieldButton.preferredSize = Dimension(100, 30)
        addSearchFieldButton.addActionListener { addSearchField() }
        // search modal buttons panel
        val buttonsPanel = JPanel(FlowLayout(FlowLayout.RIGHT))
        buttonsPanel.add(addSearchFieldButton)
        buttonsPanel.add(okButton)
        // search modal layout
        layout = BorderLayout()
        add(buttonsPanel, BorderLayout.PAGE_END)
        nextSearchFieldContainer = this
        // Adding first search field
        addSearchField()
    }

    private fun addSearchField() {
        callbacks.issueAlert("Size is ${searchFields.size}")
        val searchField = TextQueryField()
        searchFields.add(searchField)
        callbacks.issueAlert("Now it is ${searchFields.size}")
        nextSearchFieldContainer.add(searchField, BorderLayout.PAGE_START)
        val newContainer = JPanel()
        nextSearchFieldContainer.add(newContainer, BorderLayout.CENTER)
        nextSearchFieldContainer.revalidate()
        nextSearchFieldContainer.repaint();
        newContainer.layout = BorderLayout()
        nextSearchFieldContainer = newContainer

    }

    //TODO probably create a searchQuery class that holds the information needed, and pass it to the main UI
    private fun executeSearch() {
        mainUI.executeSearch(searchFields.map { sf -> sf.query })
        isVisible = false
    }

    fun display() {
        isVisible = true
    }

}