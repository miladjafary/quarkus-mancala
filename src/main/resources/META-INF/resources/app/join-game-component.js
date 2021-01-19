angular.module('components', [])

    .directive('joingame', function () {
      return {
        restrict: 'E',
        transclude: true,
        scope: {},
        templateUrl: 'app/join-game.template.html',
        replace: true,
        controller: function ($scope, $element, $http) {
          $scope.alert = {
            type: 'success',
            show: false,
            messages: [],
            reset: function () {
              this.messages = [];
              this.show = false;
            }
          };

          let onError = function (errorResponse) {
            showError(errorResponse.data.error);
          };

          let showError = function (errorMessage) {
            $scope.alert.reset();
            $scope.alert.type = 'danger';
            $scope.alert.show = true;
            $scope.alert.messages.push(errorMessage);
          }

          $scope.form = {
            playerName: "",
            gameId: "",
            validate: function () {
              if (!this.gameId) {
                showError("Game Id is required");
                return false;
              } else if (!this.playerName) {
                showError("Player Name is required");
                return false;
              }

              return true;
            }
          }

          $scope.startNewGame = function () {
            let playerName = $scope.form.playerName;
            let gameId = $scope.form.gameId;
            let addPlayerApiUrl = `/games/${gameId}/addPlayer/${playerName}`;
            if ($scope.form.validate()) {
              $http.post(addPlayerApiUrl)
                  .then(function onSuccess() {
                    $scope.alert.reset();
                    location.href="/index.html"
                  }, onError);
            }
          }
        }
      };
    });