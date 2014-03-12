package org.lsug.restlife

import scala.annotation.tailrec

object LifeSym {
  type Cell = (Int, Int)
  type Board = Set[Cell]

  def x(c: Cell) = c._1
  def y(c: Cell) = c._2

  def cell(x: Int, y: Int): Cell = (x, y)
  def board(cs: Cell*): Board = cs.toSet

  val neighbourOffsets: Set[(Int, Int)] = Set((0, -1), (1, -1), (1, 0), (1, 1), (0, 1), (-1, 1), (-1, 0), (-1, -1))

  def neighbourCoordinates(c: Cell): Set[Cell] = neighbourOffsets.map { case (ofX, ofY) => cell(x(c) + ofX, y(c) + ofY) }

  def areNeighbours(c1: Cell, c2: Cell): Boolean = neighbourCoordinates(c1).contains(c2)

  def livingNeighbours(b: Board, c: Cell): Set[Cell] = b.filter(other => areNeighbours(c, other))

  def nextGeneration(b: Board): Board = {
    // Create a set that contains all the cells that should survive as they have 2 or 3 living neighbours
    val survivingCells = b.filter { c =>
      val neighboursCount = livingNeighbours(b, c).size
      neighboursCount == 2 || neighboursCount == 3
    }

    // Create a set that contains all the cells that should be created as they are near
    // three living cells
    val spawiningCells = b.flatMap(neighbourCoordinates).filter { c =>
      !b.contains(c) && livingNeighbours(b, c).size == 3
    }

    // The next generation is the union of the above sets
    survivingCells ++ spawiningCells
  }

  def sym(b: Board, steps: Int): List[Board] = {
    // Define a tail-recursive private function that runs the evolution a given number
    // of times and accumulate calculated states
    @tailrec
    def runBoard(b: Board, s: Int, states: List[Board] = Nil): List[Board] =
      if (s == 0) states
      else runBoard(nextGeneration(b), s - 1, b :: states)

    // Use the defined function to calculate the states
    runBoard(b, steps)
  }

  def symStream(board: Board): Stream[Board] = Stream.iterate(board)(b => LifeSym.nextGeneration(b))
}
