package com.szachmaty.gamelogicservice.infrastructure.controller.data;


public record GameCreateReq(String gameMode,
                            String gameTime,
                            String player1,
                            String player2,
                            String player1PieceColor
) {
}
