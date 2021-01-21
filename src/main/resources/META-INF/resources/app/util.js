GameWebSocket = {
  gameNotifierUrl: 'ws://' + window.location.host + '/gameNotifier',
  websocket: null,
  onBoardChange: function (message) {
    console.log("BoardChange");
    console.log(message);
  },
  onGameStarted: function (message) {
    console.log("gameStarted");
    console.log(message);
  },
  onGameOver: function (message) {
    console.log("game over");
    console.log(message);
  },

  open: function () {
    let _this = this;
    _this.websocket = new WebSocket(_this.gameNotifierUrl);
    _this.websocket.onmessage = function (serverMessage) {
      let GAME_STARTED = 'GAME_STARTED',
          GAME_OVER = 'GAME_OVER',
          GAME_BOARD_CHANGE = 'GAME_BOARD_CHANGE';

      let eventObject = JSON.parse(serverMessage.data);
      let event = eventObject.event;

      switch (event) {
        case GAME_BOARD_CHANGE:
          _this.onBoardChange(eventObject);
          break;
        case GAME_STARTED:
          _this.onGameStarted(eventObject);
          break;
        case GAME_OVER:
          _this.onGameOver(eventObject);
          break;
      }
    };
  }
}

function newGameWebSocket() {
  return GameWebSocket;
}
