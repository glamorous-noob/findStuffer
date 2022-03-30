package burp.findstuffer

import burp.findstuffer.TextQueryScope.Companion.SCOPES
import org.w3c.dom.Text
import java.awt.BorderLayout
import java.awt.Color
import javax.swing.BorderFactory
import javax.swing.JComboBox
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

// TODO this.
// Just a placeholder, will transform into a real proper class later.
class TextQueryField : JPanel() {
    private val textField = JTextField()
    private val label = JLabel("Text query : ")
    private val scopeChooser = JComboBox(SCOPES)
    val query : TextQuery
        get() = TextQuery(textField.text, SCOPES[scopeChooser.selectedIndex], false)

    init {
        border = BorderFactory.createEmptyBorder(4, 4, 4, 4)
        layout = BorderLayout()
        textField.border = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(4, 3, 0, 0)
        )
        scopeChooser.selectedIndex = SCOPES.indexOf(TextQueryScope.REQUEST_OR_RESPONSE)
//        scopeChooser.addActionListener(this)
        add(label, BorderLayout.LINE_START)
        add(textField, BorderLayout.CENTER)
        add(scopeChooser, BorderLayout.LINE_END)
    }

}