package vhky.foundation

import javafx.fxml.FXML
import java.net.URL
import java.util.*

/**
 * No Description
 *
 * Created at 14:15 2017/7/31
 * @author VHKY
 */
open class Controller
{
	@FXML protected lateinit var resources : ResourceBundle
	@FXML protected lateinit var location : URL
	@FXML protected open fun initialize() = Unit
}