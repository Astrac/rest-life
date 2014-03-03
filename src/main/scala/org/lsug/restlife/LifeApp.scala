import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.value.ObservableBooleanValue
import javafx.scene.layout.Pane
import scalafx.application.JFXApp.PrimaryStage
import scalafx.beans.property.ReadOnlyBooleanProperty
import scalafx.geometry.Insets
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.{AnchorPane, FlowPane, BorderPane}
import scalafx.scene.Scene
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scalafx.stage.Stage

class Cell(val rx: Double, val ry: Double) extends Rectangle {
  var alive = new SimpleBooleanProperty()
  def alive_=(v: Boolean) { alive() = v }

  x = rx * 10
  y = ry * 10
  width = 8
  height = 8
  fill <== when (hover) choose Color.gray(0.8) otherwise (when (alive) choose Color.gray(1) otherwise Color.gray(0.4))
  arcWidth = 2
  arcHeight = 2
}

object LifeApp extends JFXApp {
  def cell(rx: Double, ry: Double, a: Boolean = false) = new Cell(rx, ry) {
    alive = a
  }

  stage = new PrimaryStage {
    scene = new Scene {
      root = new BorderPane {
        fill = Color.gray(0.1)
        center = new AnchorPane {
          content = for {
            rx <- 0 to 100
            ry <- 0 to 70
          } yield cell(rx, ry, math.random > 0.7)
        }
        bottom = new BorderPane {
          padding = Insets(5)
          center = new Button("Start")
        }
      }
    }
  }
}
