import com.intellij.ide.ui.LafManager
import com.intellij.ide.ui.LafManagerListener
import javax.swing.UIManager

class ProgressBarLafManagerListener : LafManagerListener {

    init {
        updateProgressBarUI()
    }

    override fun lookAndFeelChanged(lafManager: LafManager) {
        updateProgressBarUI()
    }

    companion object {

        private fun updateProgressBarUI() {
            val defaults = UIManager.getLookAndFeelDefaults()
            defaults["ProgressBarUI"] = ProgressBarUi::class.java.name
            defaults[ProgressBarUi::class.java.name] = ProgressBarUi::class.java
        }
    }
}
