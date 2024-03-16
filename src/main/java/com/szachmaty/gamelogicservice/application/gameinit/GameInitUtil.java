package com.szachmaty.gamelogicservice.application.gameinit;


import java.util.UUID;

public final class GameInitUtil {

    private final static long HOUR_IN_SEC = 3600;
    private final static long MIN_IN_SEC = 60;

    /**
     * @return uuid
     */
    public static String generateGameCode() {
        final String uuid = UUID.randomUUID().toString();
        return uuid.replace("-" , "").substring(0, 12);
    }

    /**
     * @param gameTime
     * @return LocalTime in format h:min:sec or min:sec
     */
    public static Long gameTimeParser(Integer gameTime) {
        return Long.valueOf(gameTime*60);
    }
}
