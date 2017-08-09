package vhky.application

import javafx.application.Application
import javafx.scene.image.Image
import javafx.stage.Stage

class Main : Application()
{
	val main by lazy { MainController.Load() }
	override fun start(stage : Stage)
	{
		main.stage = stage
		stage.title = "Fuck"
		stage.icons.add(Image("/resource/picture/icon.png"))
		stage.minHeight = stage.height
		stage.minWidth = stage.width
		
		main.init()
	}
}
