package com.szachmaty.gamelogicservice.controller;


public final class APIRoutes {
    public final static String GAME_INIT = "/game-init";
    public final static String GAME_INFO = "/game-info/{gameCode}";

    public final static String MOVE = "/move";

    public final static String GAME_CHECK_PLAYER = "/game-check-player";
    public final static String GAME_FINISH = "/game";

    public final static String WSS_HANDSHAKE = "/game-handshake";
    public final static String GAME_MESSAGE = "/app";
    public final static String QUEUE_URL = "/queue/move/";
}
