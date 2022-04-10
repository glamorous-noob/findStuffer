package burp.findstuffer.search

import burp.findstuffer.search.TextQueryScope.Companion.SCOPES
import java.awt.*
import javax.swing.*

class TextQueryField(removable: Boolean, searchModal: SearchModal, private val id: Int) : JPanel() {
    var programmedForDeletion = false
    private val textField = JTextField()
    private val label = JLabel("Text query : ")
    private val scopeChooser = JComboBox(SCOPES)
    val notCheckbox = JCheckBox("Inverted")
    private val negativeQuery: Boolean
        get() = notCheckbox.isSelected

    // TODO is using a computed member here a good idea performance-wise ? It's certainly an easy idea
    val query: TextQuery
        get() = TextQuery(textField.text, SCOPES[scopeChooser.selectedIndex], negativeQuery)

    init {
        border = BorderFactory.createEmptyBorder(4, 4, 4, 4)
        layout = BorderLayout()
        textField.border = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(4, 3, 0, 0)
        )
        scopeChooser.selectedIndex = SCOPES.indexOf(TextQueryScope.REQUEST_OR_RESPONSE)
        add(label, BorderLayout.LINE_START)
        add(textField, BorderLayout.CENTER)
        val optionsPanel = JPanel(FlowLayout(FlowLayout.LEFT, 2, 0))
        optionsPanel.add(scopeChooser)
        notCheckbox.isSelected = false
        optionsPanel.add(notCheckbox)
        if (removable) optionsPanel.add(createDeleteButton(searchModal))
        add(optionsPanel, BorderLayout.LINE_END)
    }


    private fun createDeleteButton(searchModal: SearchModal): Component {
        val deleteFieldButton = JButton(
            ImageIcon(
                ImageIcon(
                    SearchModal::class.java.classLoader.getResource("deleteField.png")
                ).image.getScaledInstance(10, 10, Image.SCALE_SMOOTH)
            )
        )
        deleteFieldButton.preferredSize = Dimension(20, 20)
        deleteFieldButton.addActionListener { searchModal.programForDeletion(id) }
        return deleteFieldButton
    }

    fun complyToCache(cache: TextQuery) {
        textField.text = cache.text
        scopeChooser.selectedIndex = SCOPES.indexOf(cache.scope)
        notCheckbox.isSelected = cache.negativeQuery
    }

}