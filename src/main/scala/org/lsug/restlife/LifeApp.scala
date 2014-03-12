import javafx.beans.property.SimpleBooleanProperty
import javafx.event.{ActionEvent, EventHandler}
import javafx.scene.input.MouseEvent
import org.lsug.restlife.LifeSym
import org.lsug.restlife.LifeSym.Board
import scalafx.animation.{AnimationTimer, Interpolator, Timeline}
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.control.Button
import scalafx.scene.layout.{AnchorPane, BorderPane}
import scalafx.scene.Scene
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle

object LifeApp extends JFXApp {
  val selecting = new SimpleBooleanProperty()
  val running = new SimpleBooleanProperty()
  var lastTime = 0L
  val timer = AnimationTimer { now: Long =>
    if (now - lastTime >= 200000000) {
      setBoard(LifeSym.nextGeneration(board))
      lastTime = now
    }
  }

  val startStopButton = new Button("Start") {
    text <== when(running) choose "Stop" otherwise "Start"
    onAction = new EventHandler[ActionEvent] {
      def handle(p1: ActionEvent): Unit = {
        running() = !running()
        if (running()) {
          timer.start()
        } else {
          timer.stop()
        }
      }
    }
  }

  class Cell(val rx: Int, val ry: Int) extends Rectangle {
    val alive = new SimpleBooleanProperty()
    def alive_=(v: Boolean) { alive() = v }

    def update(b: Board) = b.contains(LifeSym.cell(rx, ry))

    onMouseClicked = new EventHandler[MouseEvent] {
      def handle(p1: MouseEvent): Unit = if (!running()) { alive() = !alive() }
    }

    x = rx * 10
    y = ry * 10
    width = 8
    height = 8
    fill <== when (hover && running.not()) choose Color.gray(0.8) otherwise (when (alive) choose Color.gray(1) otherwise Color.gray(0.4))
    arcWidth = 2
    arcHeight = 2
  }

  def cell(rx: Int, ry: Int, a: Boolean = false) = new Cell(rx, ry) {
    alive() = a
  }

  val cells = for {
    rx <- 0 to 100
    ry <- 0 to 70
  } yield cell(rx, ry, false)

  def board: Board = cells.filter(_.alive()).map(c => LifeSym.cell(c.rx, c.ry)).toSet

  def setBoard(b: Board): Unit = {
    cells.foreach(c => c.alive() = b.contains(LifeSym.cell(c.rx, c.ry)))
  }

  stage = new PrimaryStage {
    scene = new Scene {
      root = new BorderPane {
        fill = Color.gray(0.1)
        center = new AnchorPane {
          content = cells
        }
        bottom = new BorderPane {
          padding = Insets(5)
          center = startStopButton
        }
      }
    }
  }
}
