package vhky.application

import javafx.application.Application
import javafx.scene.image.Image
import javafx.stage.Stage

class Main : Application()
{
	private val main by lazy { MainController.Load() }
	override fun start(stage : Stage)
	{
		main.stage = stage
		stage.title = """Edge Detection, Author = si_dv_vk@foxmail.com"""
		stage.icons.add(Image("/vhky/resource/picture/icon.png"))
		stage.minHeight = stage.height
		stage.minWidth = stage.width
		
		main.init()
	}
}
