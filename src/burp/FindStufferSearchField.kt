package burp

import java.awt.BorderLayout
import java.awt.Color
import javax.swing.BorderFactory
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

// TODO this.
// Just a placeholder, will transform into a real proper class later.
class FindStufferSearchField : JPanel() {
    private val textField = JTextField()
    private val label = JLabel("Text query : ")
    val text : String
        get() = textField.text

    init {
        layout = BorderLayout()
        textField.border = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(4, 3, 0, 0)
        )
        add(label, BorderLayout.LINE_START)
        add(textField, BorderLayout.CENTER)
    }

}