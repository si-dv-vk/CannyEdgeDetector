package vhky.application

import javafx.application.Platform
import javafx.embed.swing.SwingFXUtils
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.image.WritableImage
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import vhky.Configuration
import vhky.algorithm.data.GrayScaleFactory
import vhky.algorithm.edge.CannyEdgeDetector
import vhky.foundation.Controller
import java.io.File
import javax.imageio.ImageIO
import kotlin.concurrent.thread


class MainController : Controller()
{
	companion object
	{
		fun Load() : MainController
		{
			val loader = FXMLLoader(this::class.java.getResource("/vhky/resource/fxml/main.fxml"))
			val root = loader.load<VBox>()
			return loader.getController<MainController>().apply { this.root = root }
		}
		
	}
	
	lateinit var root : VBox
	var stage : Stage? = null
	get() = field
	set(value)
	{
		field = value
		field?.scene = Scene(root)
		field?.show()
	}
	private @FXML lateinit var leftPane : StackPane
	private @FXML lateinit var rightPane : StackPane
	private @FXML lateinit var bottomPane : HBox
	private @FXML lateinit var leftImage : ImageView
	private @FXML lateinit var rightImage : ImageView
	private @FXML lateinit var fuckIt : Button
	private @FXML lateinit var prepare : Button
	private @FXML lateinit var strongThreshold : TextField
	private @FXML lateinit var weakThreshold : TextField
	private val image = Image("/vhky/resource/picture/test.jpg")
	private val bottomPaneHeight by lazy { fuckIt.height + 20 }
	fun init()
	{
		layoutConstraints()
		stage?.width = 800.0
		stage?.height = 600.0
		leftImage.image = image
		rightImage.image = WritableImage(image.pixelReader, image.width.toInt(), image.height.toInt())
		rightImage.onMouseDragged = ImageCircleListener(rightImage)
	}
	private fun layoutConstraints()
	{
		root.isFillWidth = true
		root.heightProperty().addListener { _, _, new ->
			(new as? Double)?.let()
			{
				leftPane.prefHeight = it - bottomPaneHeight
				rightPane.prefHeight = it - bottomPaneHeight
				leftImage.fitHeight = it - bottomPaneHeight
				rightImage.fitHeight = it - bottomPaneHeight
			}
		}
		root.widthProperty().addListener { _, _, new ->
			(new as? Double)?.let()
			{
				rightPane.prefHeight= it / 2
				leftPane.prefWidth = it / 2
				leftImage.fitWidth = it / 2 - 1.0
				rightImage.fitWidth = it / 2 - 1.0
			}
		}
		bottomPane.minHeight = bottomPaneHeight
		bottomPane.maxHeight = bottomPaneHeight
		bottomPane.prefHeight = bottomPaneHeight
	}
	@FXML private fun onFuckIt()
	{
		temp?.let()
		{
			val text = fuckIt.text
			fuckIt.text = "Processing..."
			fuckIt.isDisable = true
			thread()
			{
				val _image = CannyEdgeDetector.postprocess(it,
						{ it[(it.size * strongThreshold.text.toDouble()).toInt()] to
								it[(it.size * strongThreshold.text.toDouble()).toInt()] / weakThreshold.text.toDouble() }).let { GrayScaleFactory.toImage(it) }
				Platform.runLater()
				{
					rightImage.image = _image
					fuckIt.isDisable = false
					fuckIt.text = text
				}
			}
		}
	}
	
	var temp : CannyEdgeDetector.PreprocessResult? = null
	@FXML private fun onPrepare()
	{
		val text = prepare.text
		prepare.text = "Processing..."
		prepare.isDisable = true
		thread()
		{
			temp = CannyEdgeDetector.preprocess(GrayScaleFactory.fromImage(rightImage.image))
			Platform.runLater()
			{
				prepare.isDisable = false
				prepare.text = text
			}
		}
	}
	@FXML private fun onRestore()
	{
		rightImage.image = WritableImage(image.pixelReader, image.width.toInt(), image.height.toInt())
	}
	@FXML private fun onSave() = thread{ ImageIO.write(SwingFXUtils.fromFXImage(rightImage.image, null), "png",
			File(Configuration.TestImageOutputDir, """test_${strongThreshold.text}_${weakThreshold.text}.png""").apply { if (!exists()) createNewFile() }) }
	
}