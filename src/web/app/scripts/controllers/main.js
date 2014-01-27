'use strict';

angular.module('webApp')
  .controller('MainCtrl', function ($scope, LifeSym) {
    var boardConfig = {
      v: 40,
      h: 60,
      cellSize: 15,
      cellMargin: 0
    };

    var dist = function(index) {
      return boardConfig.cellMargin + index * (boardConfig.cellSize + 2 * boardConfig.cellMargin);
    };

    var cellFactory = function(x, y) {
      return {
        x: x,
        y: y,
        posX: dist(x),
        posY: dist(y),
        l: boardConfig.cellSize
      };
    };

    var cells = function() {
      cells = [];
      for (var x = 0; x < boardConfig.h; x++) {
        for (var y = 0; y < boardConfig.v; y++) {
          cells.push(cellFactory(x, y));
        }
      }

      return cells;
    };

    var board = {
      cells: cells(),
      selectedCells: [],
      class: function(cell) {
        return _.find(board.selectedCells, function(c) { return c.x == cell.x && c.y == cell.y; }) ? "cell-live" : "cell";
      }
    };

    var toSymData = function(board, steps) {
      return _.map(board, function(c) { return [c.x, c.y]; });
    }

    var symCells = function(step) {
      return _.map(step, function(s) { return cellFactory(s[0], s[1]); });
    }

    $scope.running = false;

    $scope.startSym = function() {
      LifeSym.startSym(toSymData(board.selectedCells), function(cells) {
        board.selectedCells = symCells(cells);
      });

      $scope.running = true;
    };

    $scope.stopSym = function() {
      LifeSym.stopSym();
      $scope.running = false;
    }

    $scope.toggle = function(c) {
      var idx = board.selectedCells.indexOf(c);
      if (idx == -1) {
        board.selectedCells.push(c);
      } else {
        board.selectedCells.splice(idx, 1);
      }
    }

    var selecting = false;
    $scope.startSelecting = function() { selecting = true; };
    $scope.endSelecting = function() { selecting = false; };
    $scope.updateSelection = function(cell) { if (selecting) $scope.toggle(cell); }

    $scope.clearBoard = function() { board.selectedCells = []; };

    $scope.board = board;
  });
