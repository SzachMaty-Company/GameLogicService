# GameLogicService

Microservice component designed to provide a chess-playing API.

## Tech Stack:
- Java 17
- Spring Boot 3.2.2
- Spring Security 6.2.1
- Spring Aop 
- Spring Data Jpa 
- Spring Boot Validation
- Lombok
- ModelMapper
- Feign Client
- Web Sockets (Stomp) API
- Gradle
- Redis
- chesslib

## API

### Http

- POST /game-init - to initialize game

```json
{
    "gameMode" : "<string>",
    "gameTime" : "<string>",
    "player1PieceColor" : "<string>",
    "player1" : "<string>",
    "player2" : "<string>"
}
```

```json
{
  "gameCode" : "<string>"
}
```

- GET /game-info/{gameCode} - get info about game

```json
{
    "fen" : "<string>",
    "whiteTime" : "<string>",
    "blackTime" : "<string>",
    "sideToMove" : "<string>",
    "gameStatus" : "<string>",
    "playerColor" : "<string>"
}
```

### WebSocket

- /game-handshake - enable websocket connection

Example (written in JavaScript):

```
  client = new StompJs.Client({
    brokerURL: "ws://localhost:8000/game-handshake",
    connectHeaders: {
        token: jwtToken,
        gameCode : gameCode
    }
  })
```
- MESSAGE /queue/move/${gameCode} - for receiving game-message:

```json
{
  "move" : "<string>",
  "fen" : "<string>",
  "time" : "<string>",
  "nextMoveSide" : "<string>",
  "gameStatus" : "<string>",
  "errorMessage" : "<null> | <string>"
}
```

- SEND /game/move - for sending messages

Payload:

```json
  {
    "gameCode": "<string>",
    "move": "<string>"
  }
```

