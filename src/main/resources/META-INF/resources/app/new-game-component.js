angular.module('components', [])

    .directive('newgame', function () {
      return {
        restrict: 'E',
        transclude: true,
        scope: {},
        templateUrl: 'app/new-game.template.html',
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

          $scope.visibleFormPanel = true;
          $scope.waitingPanel = {
            visible: false,
            gameUrl: "",
            gameId: "",
            playerName: "",
            reset: function () {
              this.visible = false;
              this.gameUrl = "";
              this.gameId = "";
              this.playerName = "";
            }
          };

          let onError = function (errorResponse) {
            showError(errorResponse.data.error);
          };

          let showError= function (errorMessage) {
            $scope.alert.reset();
            $scope.alert.type = 'danger';
            $scope.alert.show = true;
            $scope.alert.messages.push(errorMessage);
          }

          let createNewGame = function (onSuccessCallback) {
            $http.post('/games/')
                .then(function onSuccess(response) {
                  onSuccessCallback(response.data);
                }, onError);
          }

          $scope.startNewGame = function () {
            let playerName = $scope.playerName;

            if (!playerName) {
              showError("Player Name is required")
            } else {
              createNewGame(function (response) {
                let gameId = response.id;
                let gameUrl = response.url;

                let addPlayerApiUrl = `/games/${gameId}/addPlayer/${playerName.trim()}`;

                $http.post(addPlayerApiUrl)
                    .then(function onSuccess() {
                      $scope.alert.reset();

                      $scope.visibleFormPanel = false;
                      $scope.waitingPanel.visible = true;
                      $scope.waitingPanel.gameUrl = gameUrl;
                      $scope.waitingPanel.gameId = gameId;
                      $scope.waitingPanel.playerName = playerName;
                    }, onError);
              });
            }
          }

          $scope.restartGame = function () {
            $scope.visibleFormPanel = true;
            $scope.waitingPanel.reset();
            $scope.playerName = "";

          }
        }
      };
    });