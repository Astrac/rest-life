package org.lsug.restlife.test

import org.lsug.restlife.LifeSym
import org.scalatest._

class LifeSymTest extends FunSpec with Matchers {

  import LifeSym._

  describe("LifeSym") {
    it("should correctly identify neighbouring coordinates") {
      neighbourCoordinates(cell(1, 1)) should equal(
        Set(cell(0, 0), cell(0, 1), cell(0, 2), cell(1, 0),
          cell(1, 2), cell(2, 0), cell(2, 1), cell(2, 2)))
    }

    it("should correctly tell if two cells are neighbours or not") {
      areNeighbours(cell(2, 3), cell(4, 5)) should be (false)
      neighbourCoordinates(cell(4, 5)) foreach {
        c => areNeighbours(c, cell(4, 5)) should be (true)
      }
    }

    it("should produce a list of living neighbours of a cell") {
      val simpleBoard = board(cell(2, 3), cell(4, 5), cell(5, 6))
      livingNeighbours(simpleBoard, cell(2, 3)) should be (empty)
      livingNeighbours(simpleBoard, cell(4, 5)) should equal(Set(cell(5, 6)))
      livingNeighbours(simpleBoard, cell(5, 6)) should equal(Set(cell(4, 5)))
    }

    it("should kill cells having less than two neighbours") {
      // All isolated cells
      nextGeneration(board(cell(1, 0), cell(3, 5), cell(9, 10))) should be (empty)
      // One neighbours cells
      nextGeneration(board(cell(0, 0), cell(0, 1))) should be (empty)
    }

    it("should allow cells with two or three neighbours to survive") {
      // (0, 1) has 2 neighbours
      nextGeneration(board(cell(0, 0), cell(0, 1), cell(0, 2))) should contain((0, 1))
      // (1, 1) has 3 neighbours
      nextGeneration(board(cell(0, 0), cell(1, 1), cell(2, 2), cell(0, 2))) should contain((1, 1))
    }

    it("should kill cells that have more than three neighbours") {
      val c = cell(1, 1)
      val neighbours = LifeSym.neighbourCoordinates(c)

      val boards = for {
        i <- 4 until 9
      } yield {
        neighbours.take(i)
      }

      boards.foreach { b => nextGeneration(b) should not contain(c)}
    }

    it("should create new cells at positions with 3 alive neighbours") {
      // A board with three cells in a corner shape
      val b = board(cell(0, 0), cell(1, 1), cell(1, 0))
      val evolution = nextGeneration(b)

      evolution.size should equal(4)
      evolution should equal(b + cell(0, 1))
    }

    it("should correctly evolve a 3-cells vertical oscillator") {
      val b = board(cell(3, 2), cell(3, 3), cell(3, 4))
      val bs = sym(b, 4)

      bs should equal {
        board(cell(2, 3), cell(3, 3), cell(4, 3)) ::
        board(cell(3, 2), cell(3, 3), cell(3, 4)) ::
        board(cell(2, 3), cell(3, 3), cell(4, 3)) ::
        board(cell(3, 2), cell(3, 3), cell(3, 4)) :: Nil
      }
    }
  }
}
