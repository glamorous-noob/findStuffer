package burp.findstuffer

import java.awt.BorderLayout
import java.awt.Color
import javax.swing.BorderFactory
import javax.swing.JComboBox
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

// TODO this.
// Just a placeholder, will transform into a real proper class later.
class TextSearchField : JPanel() {
    private val textField = JTextField()
    private val label = JLabel("Text query : ")
    private val scopeChooser = JComboBox(SearchQueryScope.values())
    val text : String
        get() = textField.text
    val scope : SearchQueryScope
        get() = SearchQueryScope.values()[scopeChooser.selectedIndex]

    init {
        border = BorderFactory.createEmptyBorder(4, 4, 4, 4)
        layout = BorderLayout()
        textField.border = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(4, 3, 0, 0)
        )
        scopeChooser.selectedIndex = 0
//        scopeChooser.addActionListener(this)
        add(label, BorderLayout.LINE_START)
        add(textField, BorderLayout.CENTER)
        add(scopeChooser, BorderLayout.LINE_END)
    }

}