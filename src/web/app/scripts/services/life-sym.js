'use strict';

angular.module('webApp')
  .service('LifeSym', function LifeSym($interval, $http) {
    var symPromise = null;
    var steps = [];
    var requesting = null;

    var requestSteps = function(symData) {
      if (requesting) return;

      requesting = $http({method: 'POST', data: { board: symData, steps: 100 }, url: '/board'})
        .success(function(data) {
          steps = data.concat(steps);
          requesting = null;
        })
        .error(function() { requesting = null });
    };

    var startSym = function(selectedCells, handler) {
      if (symPromise !== null) {
        throw new Error("Symulation is already running");
      }

      symPromise = $interval(function() {
        if (steps.length > 0) {
          var nextStep = _.last(steps);
          steps = _.without(steps, nextStep);

          console.log(steps.length);
          handler(nextStep);
        }
        if (steps.length == 0) {
          requestSteps(selectedCells);
        } else if (steps.length < 40) {
          requestSteps(_.first(steps));
        }
      }, 250);
    };

    return {
      startSym: startSym,
      stopSym: function() {
        $interval.cancel(symPromise);
        symPromise = null;
        steps = [];
      }
    }
  });
