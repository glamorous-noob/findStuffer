package burp

import javax.swing.SwingUtilities

//No need to implement a listener for now, the main use case is doing searches on existing logs.
@SuppressWarnings("unused") // Remove warning, the class will be used by burp
class BurpExtender : IBurpExtender {
    private lateinit var ui: FindStufferUI
    companion object {
        lateinit var callbacks: IBurpExtenderCallbacks
    }

    override fun registerExtenderCallbacks(callbacks: IBurpExtenderCallbacks) {
        Companion.callbacks = callbacks
        // Set our extension name, this will be display in burp extensions tab
        callbacks.setExtensionName("FindStuffer")

        SwingUtilities.invokeLater {
            ui = FindStufferUI()
            ui.setHistoryTableContent(callbacks.proxyHistory)
            callbacks.addSuiteTab(ui)
        }
    }

}