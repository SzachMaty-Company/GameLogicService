package com.szachmaty.gamelogicservice.data.dto;


public record GameInitRequest(GameMode gameMode,
                              Integer gameTime,
                              String player1,
                              String player2,
                              PlayerColor player1PieceColor
) { }
