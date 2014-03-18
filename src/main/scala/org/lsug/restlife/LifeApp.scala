import javafx.beans.property.SimpleBooleanProperty
import org.lsug.restlife.LifeSym
import org.lsug.restlife.LifeSym.Board
import scalafx.animation.AnimationTimer
import scalafx.application.JFXApp.PrimaryStage
import scalafx.event.ActionEvent
import scalafx.geometry.Insets
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.control.Button
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.{AnchorPane, BorderPane}
import scalafx.scene.Scene
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle

object LifeApp extends JFXApp {
  // Whether or not we are running a simulation at the moment
  val running = new SimpleBooleanProperty()
  // Last time a new frame has been rendered
  var lastTime = 0L

  // The timer object that will be used to update the board during the simulation
  val timer = AnimationTimer { now: Long =>
    // Push a new frame every 200 ms
    if (now - lastTime >= 200000000) {
      setBoard(LifeSym.nextGeneration(board))
      lastTime = now
    }
  }

  // A button to allow starting/stopping the simulation
  val startStopButton = new Button("Start") {
    // Conditional binding of ScalaFX properties
    text <== when(running) choose "Stop" otherwise "Start"
    // Handler for the button press event
    onAction = (p1: ActionEvent) => {
      running() = !running()
      if (running()) {
        timer.start()
      } else {
        timer.stop()
      }
    }
  }

  // Class that represents a cell in the UI at the given board coordinates
  class Cell(val rx: Int, val ry: Int) extends Rectangle {
    // Whether or not the cell is alive
    val alive = new SimpleBooleanProperty()

    // A setter for the alive property
    def alive_=(v: Boolean) { alive() = v }

    // If the simulation is not running switch the status of the cell on click
    onMouseClicked = (p1: MouseEvent) => if (!running()) { alive() = !alive() }

    // Positioning and sizing properties
    x = rx * 10
    y = ry * 10
    width = 8
    height = 8

    // Nice rounded corners
    arcWidth = 2
    arcHeight = 2

    // Conditional property binding for the cell background colour
    fill <== when (hover && running.not()) choose Color.gray(0.6) otherwise (when (alive) choose Color.gray(0.9) otherwise Color.gray(0.1))
  }

  // Create a new cell instance with the given board coordinates and the given status
  def cell(rx: Int, ry: Int, a: Boolean = false) = new Cell(rx, ry) {
    alive() = a
  }

  // Initialise a 100x70 UI-board of dead cells
  val cells = for {
    rx <- 0 to 100
    ry <- 0 to 70
  } yield cell(rx, ry, false)

  // Creates a logical board from the current UI-board state
  def board: Board = cells.filter(_.alive()).map(c => LifeSym.cell(c.rx, c.ry)).toSet

  // Updates the UI-board cells' status with the given logical board
  def setBoard(b: Board): Unit = {
    cells.foreach(c => c.alive() = b.contains(LifeSym.cell(c.rx, c.ry)))
  }

  // Initialise the UI
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
