package burp.findstuffer.search

import burp.findstuffer.search.TextQueryScope.Companion.SCOPES
import java.awt.*
import javax.swing.*

class TextQueryField(removable: Boolean, searchDialog: SearchDialog, private val id: Int) : JPanel() {
    var programmedForDeletion = false
    private val textField = JTextField()
    private val label = JLabel("Text query : ")
    private val scopeChooser = JComboBox(SCOPES)
    private val notCheckbox = JCheckBox("Inverted")
    private val negativeQuery: Boolean
        get() = notCheckbox.isSelected
    private val caseCheckbox = JCheckBox("Case-sensitive")
    private val caseSensitiveQuery: Boolean
        get() = caseCheckbox.isSelected

    // TODO is using a computed member here a good idea performance-wise ? It's certainly an easy idea
    val query: TextQuery
        get() = TextQuery(textField.text, SCOPES[scopeChooser.selectedIndex], negativeQuery, caseSensitiveQuery)

    init {
        border = BorderFactory.createEmptyBorder(4, 4, 4, 4)
        layout = BorderLayout()
        textField.border = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(4, 3, 0, 0)
        )
        add(label, BorderLayout.LINE_START)
        add(textField, BorderLayout.CENTER)
        val optionsPanel = JPanel(FlowLayout(FlowLayout.LEFT, 2, 0))
        optionsPanel.add(scopeChooser)
        optionsPanel.add(caseCheckbox)
        optionsPanel.add(notCheckbox)
        if (removable) optionsPanel.add(createDeleteButton(searchDialog))
        add(optionsPanel, BorderLayout.LINE_END)
        initValues()
    }


    private fun createDeleteButton(searchDialog: SearchDialog): Component {
        val deleteFieldButton = JButton(
            ImageIcon(
                ImageIcon(
                    SearchDialog::class.java.classLoader.getResource("deleteField.png")
                ).image.getScaledInstance(10, 10, Image.SCALE_SMOOTH)
            )
        )
        deleteFieldButton.preferredSize = Dimension(20, 20)
        deleteFieldButton.addActionListener { searchDialog.programForDeletion(id) }
        return deleteFieldButton
    }

    fun complyToCache(cache: TextQuery) {
        textField.text = cache.text
        scopeChooser.selectedIndex = SCOPES.indexOf(cache.scope)
        notCheckbox.isSelected = cache.negativeQuery
        caseCheckbox.isSelected = cache.caseSensitiveQuery
    }

    fun initValues() {
        textField.text = ""
        scopeChooser.selectedIndex = SCOPES.indexOf(TextQueryScope.REQUEST_OR_RESPONSE)
        notCheckbox.isSelected = false
        caseCheckbox.isSelected = false
    }

}