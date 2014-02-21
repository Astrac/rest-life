package org.lsug.restlife.test

import org.lsug.restlife.LifeSym
import org.scalatest._

class BoardTest extends FunSpec with Matchers {

  import LifeSym._

  val simpleBoard = Set(cell(2, 3), cell(4, 5), cell(5, 6))
  val noNeighboursBoard = board(cell(1, 0), cell(3, 5), cell(9, 10))
  val singleNeighbourBoard = board(cell(0, 0), cell(0, 1))

  describe("LifeSym") {
    it("correctly identify neighbouring coordinates") {
      neighbourCoordinates(cell(1, 1)) should equal(
        Set(cell(0, 0), cell(0, 1), cell(0, 2), cell(1, 0),
          cell(1, 2), cell(2, 0), cell(2, 1), cell(2, 2)))
    }

    it("correctly tell if two cells are neighbours or not") {
      areNeighbours(cell(2, 3), cell(4, 5)) should be (false)
      neighbourCoordinates(cell(4, 5)) foreach {
        c => areNeighbours(c, cell(4, 5)) should be (true)
      }
    }

    it("produce a list of living neighbours of a cell") {
      livingNeighbours(simpleBoard, cell(2, 3)) should be (empty)
      livingNeighbours(simpleBoard, cell(4, 5)) should equal(Set(cell(5, 6)))
    }

    it("create new cells at positions with 3 alive neighbours") {
      val expectedNewCell = (0, 1)
      val lifeGivingBoard = Set((0, 0), (1, 1), (1, 0))
      val nextGen = nextGeneration(lifeGivingBoard)

      nextGen.size should equal(4)
      nextGen should equal(lifeGivingBoard ++ Set(expectedNewCell))
    }

    it("kill cells that have no neighbours") {
      nextGeneration(noNeighboursBoard) should be (empty)
    }

    it("kill cells that only have one neighbour") {
      singleNeighbourBoard should be (empty)
    }

    it("c with two neighbours should survive") {
      val c00 = (0, 0)
      val c01 = (0, 1)
      val c02 = (0, 2)

      val b3 = Set(c00, c01, c02)
      val board3  = nextGeneration(b3)

      board3 should not contain((0,0))
      board3 should contain((0,1))
      board3 should not contain((0,2))
    }

    it("correctly evolve a 3 vertical pattern") {
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
