import javafx.beans.property.SimpleBooleanProperty
import org.lsug.restlife.LifeSym
import org.lsug.restlife.LifeSym.Board
import scalafx.animation.{AnimationTimer, Interpolator, Timeline}
import scalafx.application.JFXApp.PrimaryStage
import scalafx.event.ActionEvent
import scalafx.geometry.Insets
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.control.Button
import scalafx.scene.input.{MouseEvent, DragEvent, TransferMode}
import scalafx.scene.layout.{AnchorPane, BorderPane}
import scalafx.scene.{input, Scene}
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
    onAction = (p1: ActionEvent) => {
      running() = !running()
      if (running()) {
        timer.start()
      } else {
        timer.stop()
      }
    }
  }

  class Cell(val rx: Int, val ry: Int) extends Rectangle {
    val alive = new SimpleBooleanProperty()
    def alive_=(v: Boolean) { alive() = v }

    def update(b: Board) = b.contains(LifeSym.cell(rx, ry))

    onMouseClicked = (p1: MouseEvent) => if (!running()) { alive() = !alive() }

    onMouseMoved = (p1: MouseEvent) => { println(s"MouseMoved $rx - $ry") }
    onMouseEntered = (p1: MouseEvent) => { println(s"MouseEnter $rx - $ry") }
    onMouseDragEntered = (p1: MouseEvent) => { println(s"MouseDragEnter $rx - $ry") }
    onMouseDragExited = (p1: MouseEvent) => { println(s"MouseDragExited $rx - $ry") }
    onMouseDragOver = (ev: input.MouseEvent) => { println(s"MouseDragOver $rx - $ry") }
    onMouseExited = (p1: MouseEvent) => { println(s"MouseExit $rx - $ry") }
    onMouseDragged = (p1: MouseEvent) => { println(s"MouseDragged $rx - $ry") }

//    onDragOver = (ev: DragEvent) => { println(s"DragOver $rx - $ry") }
//    onDragEntered = (ev: DragEvent) => { println(s"DragEntered $rx - $ry") }
//    onMouseDragExited = (p1: MouseEvent) => { println(s"DragExited $rx - $ry") }
//    onDragDetected = (p1: MouseEvent) => { println(s"DragDetected $rx - $ry") }

    x = rx * 10
    y = ry * 10
    width = 8
    height = 8
    fill <== when (hover && running.not()) choose Color.gray(0.6) otherwise (when (alive) choose Color.gray(0.9) otherwise Color.gray(0.1))
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
        fill = Color.gray(0)
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
