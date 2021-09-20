import com.intellij.ui.scale.JBUIScale
import com.intellij.util.ui.GraphicsUtil
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UIUtil
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.TexturePaint
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.awt.geom.AffineTransform
import java.awt.geom.Area
import java.awt.geom.Rectangle2D
import java.awt.geom.RoundRectangle2D
import javax.swing.JComponent
import javax.swing.SwingConstants
import javax.swing.plaf.ComponentUI
import javax.swing.plaf.basic.BasicGraphicsUtils
import javax.swing.plaf.basic.BasicProgressBarUI

@Suppress("ACCIDENTAL_OVERRIDE")
class ProgressBarUi : BasicProgressBarUI() {

    @Volatile
    private var offset = 0

    @Volatile
    private var offset2 = 0

    @Volatile
    private var velocity = 1

    override fun getPreferredSize(c: JComponent): Dimension =
        Dimension(super.getPreferredSize(c).width, JBUI.scale(20))

    override fun installListeners() {
        super.installListeners()
        progressBar.addComponentListener(object : ComponentAdapter() {
            override fun componentShown(e: ComponentEvent) {
                super.componentShown(e)
            }

            override fun componentHidden(e: ComponentEvent) {
                super.componentHidden(e)
            }
        })
    }

    override fun paintIndeterminate(g: Graphics, c: JComponent) {
        if (g !is Graphics2D) {
            return
        }
        if (progressBar.orientation != SwingConstants.HORIZONTAL || !c.componentOrientation.isLeftToRight) {
            super.paintDeterminate(g, c)
            return
        }
        val config = GraphicsUtil.setupAAPainting(g)
        val borderInsets = progressBar.insets
        val width = progressBar.width
        var height = progressBar.preferredSize.height
        if (isOdd(c.height - height)) height++
        val barRectWidth = width - (borderInsets.left + borderInsets.right)
        val barRectHeight = height - (borderInsets.top + borderInsets.bottom)
        if (barRectWidth <= 0 || barRectHeight <= 0) {
            return
        }
        val amountFull = getAmountFull(borderInsets, barRectWidth, barRectHeight)
        val background = if (c.parent != null) c.parent.background else UIUtil.getPanelBackground()
        with(g) {
            color = background
            if (c.isOpaque) {
                fillRect(0, 0, width, height)
            }
            translate(0, (c.height - height) / 2)
            color = progressBar.foreground
            fill(RoundRectangle2D.Float(0f, 0f, width - OFF, height - OFF, R2, R2))
            color = background
            fill(RoundRectangle2D.Float(OFF, OFF, width - 3 * OFF, height - 3 * OFF, R, R))
            paint = TexturePaint(
                GraphicAssets.BACKGROUND,
                Rectangle2D.Double(
                    0.0,
                    1.0,
                    (height - 3 * OFF).toDouble(),
                    (height - 3 * OFF).toDouble()
                )
            )
            fill(
                RoundRectangle2D.Float(
                    2 * OFF,
                    2 * OFF,
                    amountFull - JBUIScale.scale(5f),
                    height - JBUIScale.scale(5f),
                    JBUIScale.scale(7f),
                    JBUIScale.scale(7f)
                )
            )
            val periodLength = JBUI.scale(16)
            val containingRoundRect = Area(RoundRectangle2D.Float(1f, 1f, width - 4f, height - 4f, R, R))
            fill(containingRoundRect)
            offset = (offset + 1) % periodLength
            offset2 += velocity
            if (offset2 <= 2) {
                offset2 = 2
                velocity = 1
            } else if (offset2 >= width - JBUI.scale(15)) {
                offset2 = width - JBUI.scale(15)
                velocity = -1
            }

            val icon = if (velocity > 0) GraphicAssets.SHREK else GraphicAssets.R_SHREK

            icon.paintIcon(progressBar, g, offset2 - JBUI.scale(3), JBUI.scale(2))
            translate(0, -(c.height - height) / 2)
        }

        if (progressBar.isStringPainted) {
            val fillStart = if (progressBar.orientation == SwingConstants.HORIZONTAL) boxRect.x else boxRect.y
            paintString(
                g = g,
                x = borderInsets.left,
                y = borderInsets.top,
                w = barRectWidth,
                h = barRectHeight,
                fillStart = fillStart,
                amountFull = boxRect.width
            )
        }
        config.restore()
    }

    override fun paintDeterminate(g: Graphics, c: JComponent) {
        if (g !is Graphics2D) {
            return
        }
        if (progressBar.orientation != SwingConstants.HORIZONTAL || !c.componentOrientation.isLeftToRight) {
            super.paintDeterminate(g, c)
            return
        }
        val config = GraphicsUtil.setupAAPainting(g)
        val borderInsets = progressBar.insets
        val width = progressBar.width
        var height = progressBar.preferredSize.height
        if (isOdd(c.height - height)) height++
        val barRectWidth = width - (borderInsets.left + borderInsets.right)
        val barRectHeight = height - (borderInsets.top + borderInsets.bottom)
        if (barRectWidth <= 0 || barRectHeight <= 0) {
            return
        }
        val amountFull = getAmountFull(borderInsets, barRectWidth, barRectHeight)
        val background = if (c.parent != null) c.parent.background else UIUtil.getPanelBackground()
        with(g) {
            color = background
            if (c.isOpaque) {
                fillRect(0, 0, width, height)
            }
            translate(0, (c.height - height) / 2)
            color = progressBar.foreground
            fill(RoundRectangle2D.Float(0f, 0f, width - OFF, height - OFF, R2, R2))
            color = background
            fill(RoundRectangle2D.Float(OFF, OFF, width - 3 * OFF, height - 3 * OFF, R, R))
            paint = TexturePaint(
                GraphicAssets.BACKGROUND,
                Rectangle2D.Double(
                    0.0,
                    1.0,
                    (height - 3 * OFF).toDouble(),
                    (height - 3 * OFF).toDouble()
                )
            )
            fill(
                RoundRectangle2D.Float(
                    2 * OFF,
                    2 * OFF,
                    amountFull - JBUIScale.scale(5f),
                    height - JBUIScale.scale(5f),
                    JBUIScale.scale(7f),
                    JBUIScale.scale(7f)
                )
            )
            GraphicAssets.SHREK.paintIcon(progressBar, g, amountFull - JBUI.scale(10), JBUI.scale(2))
            translate(0, -(c.height - height) / 2)
        }
        if (progressBar.isStringPainted) {
            paintString(
                g, borderInsets.left, borderInsets.top,
                barRectWidth, barRectHeight,
                amountFull, borderInsets
            )
        }
        config.restore()
    }

    private fun paintString(g: Graphics, x: Int, y: Int, w: Int, h: Int, fillStart: Int, amountFull: Int) {
        if (g !is Graphics2D) {
            return
        }
        val progressString = progressBar.string
        with(g) {
            font = progressBar.font
            var renderLocation = getStringPlacement(g, progressString, x, y, w, h)
            val drawProgressString = {
                BasicGraphicsUtils.drawString(
                    progressBar,
                    g,
                    progressString,
                    renderLocation.x.toFloat(),
                    renderLocation.y.toFloat()
                )
            }
            val oldClip = clipBounds
            if (progressBar.orientation == SwingConstants.HORIZONTAL) {
                color = selectionBackground
                drawProgressString.invoke()
                color = selectionForeground
                clipRect(fillStart, y, amountFull, h)
                drawProgressString.invoke()
            } else {
                color = selectionBackground
                val rotate = AffineTransform.getRotateInstance(Math.PI / 2)
                font = progressBar.font.deriveFont(rotate)
                renderLocation = getStringPlacement(g, progressString, x, y, w, h)
                drawProgressString.invoke()
                color = selectionForeground
                clipRect(x, fillStart, w, amountFull)
                drawProgressString.invoke()
            }
            g.clip = oldClip
        }
    }

    override fun getBoxLength(availableLength: Int, otherDimension: Int): Int =
        availableLength

    private fun isOdd(value: Int): Boolean =
        value % 2 != 0

    companion object {

        private val R = JBUIScale.scale(8f)
        private val R2 = JBUIScale.scale(9f)
        private val OFF = JBUIScale.scale(1f)

        @Suppress("UNUSED")
        @JvmStatic
        fun createUI(c: JComponent): ComponentUI {
            c.border = JBUI.Borders.empty().asUIResource()
            return ProgressBarUi()
        }
    }
}
