angular.module('components', [])

    .directive('gameboard', function () {
      return {
        restrict: 'E',
        transclude: true,
        scope: {},
        templateUrl: 'app/game-board.template.html',
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

          $scope.board = {
            visible: false,
            player: "",
            opponent: "",
            gameId: "",
            isYourTurn: true,
            gameOver: false,
            winner: "",
            pits: {
              "1": 1,
              "2": 2,
              "3": 3,
              "4": 4,
              "5": 5,
              "6": 6,
              "7": 7,
              "8": 8,
              "9": 9,
              "10": 10,
              "11": 11,
              "12": 12,
              "13": 13,
              "14": 14,
            }
          }

          $scope.play = function (pitIndex) {
            let gameId = $scope.board.gameId;
            let player = $scope.board.player;
            let playUrl = `/games/${gameId}/${player}/pit/${pitIndex}`;
            $http.put(playUrl)
                .then(function onSuccess() {
                  $scope.alert.reset();
                }, onError);
          }

          $scope.refreshBoard = function () {
            let gameId = $scope.board.gameId;
            let player = $scope.board.player;
            let playUrl = `/games/${gameId}/${player}`;
            if ($scope.board.gameOver) {
              showError("Game Is Over. Winner is " + $scope.board.winner);
            } else {
              $http.get(playUrl)
                  .then(function onSuccess(response) {
                    $scope.alert.reset();
                    setBoardByGameInfo(response.data);
                  }, onError);
            }
          }

          let setBoardByGameInfo = function (gameInfo) {
            $scope.board.visible = true;
            $scope.board.pits = gameInfo["boardStatus"];
            $scope.board.isYourTurn = gameInfo["nextTurn"] === $scope.board.player;
            $scope.board.gameOver = gameInfo["gameOver"];
            $scope.board.opponent = gameInfo["opponent"];
          }

          let parseQueryString = function () {
            let str = window.location.search;
            let objURL = {};

            str.replace(
                new RegExp("([^?=&]+)(=([^&]*))?", "g"),
                function ($0, $1, $2, $3) {
                  objURL[$1] = $3;
                }
            );
            return objURL;
          };

          let openWeSocket = function (gameId) {
            let webSocket = newGameWebSocket();
            webSocket.onBoardChange = function (message) {
              if (message.gameId === gameId) {
                $scope.refreshBoard();
              }
            }
            webSocket.onGameOver = function (message) {
              if (message.gameId === gameId) {
                $scope.board.winner = message.winner;
                $scope.refreshBoard();
              }
            }

            webSocket.open();
          }

          let init = function () {
            let params = parseQueryString();
            let gameId = params["gameId"];
            let player = params["player"];

            if (!gameId || !player) {
              $scope.board.visible = false;
              showError("Game Id or Player Name is invalid");
            } else {
              $scope.board.gameId = gameId;
              $scope.board.player = player;
              $scope.refreshBoard();
              openWeSocket(gameId);
            }

          }

          init();
        }
      };
    });