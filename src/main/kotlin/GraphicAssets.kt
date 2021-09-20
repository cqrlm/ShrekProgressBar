import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import javax.swing.ImageIcon

object GraphicAssets {

    @JvmField
    var SHREK = ImageIcon(GraphicAssets::class.java.getResource("/shrek.gif"))

    @JvmField
    var R_SHREK = ImageIcon(GraphicAssets::class.java.getResource("/rshrek.gif"))

    @JvmField
    val BACKGROUND: BufferedImage = ImageIO.read(GraphicAssets::class.java.getResource("/background.png"))
}
