package com.szachmaty.gamelogicservice.infrastructure.controller.data;


public record GameInitReq(String gameMode,
                          String gameTime,
                          String player1,
                          String player2,
                          String player1PieceColor
) { }
