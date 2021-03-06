package burp.findstuffer.search

import burp.findstuffer.FindStufferUI
import burp.findstuffer.rowfilters.RowFilterAggregationType
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.*


class SearchDialog(private val mainUI: FindStufferUI) :
    JDialog() {
    // searchFields[i] is the field in the PAGE_START of searchFieldContainers[i]
    // The last element of searchFieldContainers is the one anticipating the creation of a new searchField
    // That's why the containers list will have one more element than the search fields list
    private var searchFields = arrayListOf<TextQueryField>()
    private var searchFieldContainers = arrayListOf<Container>()
    private var queryCache = arrayListOf<TextQuery>()
    private var aggregationType = RowFilterAggregationType.AND

    init {
        title = "FindStuffin'"
        defaultCloseOperation = DISPOSE_ON_CLOSE
        size = Dimension(800, 300)
        // Buttons
        val okButton = JButton("OK")
        okButton.preferredSize = Dimension(100, 30)
        okButton.addActionListener { submitSearch(false) }
        val applyButton = JButton("Apply")
        applyButton.preferredSize = Dimension(100, 30)
        applyButton.addActionListener { submitSearch(true) }
        val clearSearchButton = JButton("Clear")
        clearSearchButton.preferredSize = Dimension(100, 30)
        clearSearchButton.addActionListener { clearSearchFields() }
        // Add search field button
        val addSearchFieldButton = JButton(
            ImageIcon(
                ImageIcon(
                    SearchDialog::class.java.classLoader.getResource("addButtonIcon.png")
                ).image.getScaledInstance(20, 20, Image.SCALE_SMOOTH)
            )
        )
        addSearchFieldButton.preferredSize = Dimension(100, 30)
        addSearchFieldButton.addActionListener { addSearchField(true) }


        val buttonsPanel = JPanel(FlowLayout(FlowLayout.RIGHT))
        // radio buttons
        val andRButton = JRadioButton("And")
        andRButton.actionCommand = RowFilterAggregationType.AND.toString()
        andRButton.isSelected = true
        andRButton.addActionListener { radioButtonAction(it) }
        val orRButton = JRadioButton("Or")
        orRButton.actionCommand = RowFilterAggregationType.OR.toString()
        orRButton.isSelected = false
        orRButton.addActionListener { radioButtonAction(it) }
        val radioButtonsGroup = ButtonGroup()
        radioButtonsGroup.add(andRButton)
        radioButtonsGroup.add(orRButton)
        val radioButtonsPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        radioButtonsPanel.add(andRButton)
        radioButtonsPanel.add(orRButton)
        buttonsPanel.add(radioButtonsPanel)
        buttonsPanel.add(clearSearchButton)
        buttonsPanel.add(addSearchFieldButton)
        buttonsPanel.add(applyButton)
        buttonsPanel.add(okButton)
        // search modal layout
        layout = BorderLayout()
        add(buttonsPanel, BorderLayout.PAGE_END)
        searchFieldContainers.add(this)
        // Adding first search field
        addSearchField(false)

        addWindowListener(object : WindowAdapter() {
            override fun windowClosed(e: WindowEvent) {
                searchFields.forEachIndexed { ind, sf ->
                    sf.complyToCache(queryCache[ind])
                    sf.programmedForDeletion = false
                    sf.isVisible = true
                }
            }
        })
    }

    private fun radioButtonAction(e: ActionEvent) {
        aggregationType = RowFilterAggregationType.valueOf(e.actionCommand)
    }

    // TODO improve this kinda gross russian-doll-like BorderLayout implementation with something cleaner if possible
    private fun addSearchField(removable: Boolean) {
        val containerInd = searchFieldContainers.size - 1
        val container = searchFieldContainers[containerInd]
        // Creating new text field and adding it to the expected container
        val searchField = TextQueryField(removable, this, containerInd)
        searchFields.add(searchField)
        queryCache.add(TextQuery(searchField.query))
        container.add(searchField, BorderLayout.PAGE_START)
        // Creating and adding the new container for future text fields
        val newContainer = JPanel(BorderLayout())
        container.add(newContainer, BorderLayout.CENTER)
        container.revalidate()
        container.repaint()
        searchFieldContainers.add(newContainer)
    }

    private fun submitSearch(visible: Boolean) {
        // Deleting fields programmed for deletion
        var i = 0
        while (i < searchFields.size) {
            if (searchFields[i].programmedForDeletion) removeSearchField(i)
            else i++
        }
        // Caching queries for later use
        queryCache = ArrayList(
            searchFields.map { sf -> TextQuery(sf.query) }
        )
        // Executing search with cached queries
        mainUI.executeSearch(queryCache, aggregationType)
        // Disappearing
        isVisible = visible
    }

    fun display() {
        isVisible = true
    }

    fun programForDeletion(searchFieldInd: Int) {
        if (searchFieldInd < 1) return
        val field = searchFields[searchFieldInd]
        field.programmedForDeletion = true
        searchFields[searchFieldInd].isVisible = false
    }

    private fun removeSearchField(searchFieldInd: Int) {
        if (searchFieldInd < 1) return
        val correspondingContainer = searchFieldContainers[searchFieldInd]
        // There is always a previous container (container 0 is the search modal itself), because searchFieldInd > 0
        val previousContainer = searchFieldContainers[searchFieldInd - 1]
        // In all cases, when removing the searchField i, the container i+1 exists, and should be appropriately linked
        // to the container i-1, linked list style.
        val followingContainer = searchFieldContainers[searchFieldInd + 1]
        correspondingContainer.remove(followingContainer)
        previousContainer.remove(correspondingContainer)
        previousContainer.add(followingContainer, BorderLayout.CENTER)
        previousContainer.revalidate()
        previousContainer.repaint()
        searchFields.removeAt(searchFieldInd)
        searchFieldContainers.removeAt(searchFieldInd)
    }

    private fun clearSearchFields() {
        for (i in searchFields.size - 1 downTo 1) programForDeletion(i)
        searchFields[0].initValues()
    }


}