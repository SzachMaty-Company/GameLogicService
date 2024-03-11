## GameLogicService

Run docker image
`
docker compose --file=backend-docker-compose.yml up --build gateway game-logic-service redis
`
### API
- /game-init - to initialize game
Payload:
`
  {
  "gameMode": "<string>",
  "gameTime": "<string>",
  "player1PieceColor": "<string>",
  "player1": "<string>",
  "player2": "<string>"
  }
`

- /game-handshake - connect to websocket
Headers in game-handshake:
- jwt token
- gameCode
Example: 
`
  client = new StompJs.Client({
  brokerURL: "ws://localhost:8000/game-handshake",
  connectHeaders: {
  token: jwtToken,
  gameCode : gameCode
  }
  })
`
- (stomp) SUBSCRIBE /queue/move/${gameCode} - for receiving message with structure:
Payload:
`
{
  "move": "<string>",
  "fen": "<string>",
  "time": "<string>",
  "errorMessage": "<string>"
}
`
If there is an error during move processing, errorMessage will have description of this.
- (stomp) SEND /game/move - for sending messages
Payload:
`
  "userId": "<string>",
  "gameCode": "<string>",
  "move": "<string>",
`

