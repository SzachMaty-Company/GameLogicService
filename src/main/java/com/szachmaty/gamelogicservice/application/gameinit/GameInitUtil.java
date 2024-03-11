package com.szachmaty.gamelogicservice.application.gameinit;


import com.github.bhlangonijr.chesslib.game.Game;

import java.time.LocalTime;
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
    public static Long gameTimeParser(String gameTime) {
        String[] parts = gameTime.split(":");
        if(parts.length < 3) {
            throw new GameInitException("Invalid gameTime format! Given: " + gameTime + " Expected: h:min:sec");
        }
        try {
            long hours = Long.parseLong(parts[0]);
            long minutes = Long.parseLong(parts[1]);
            long seconds = Long.parseLong(parts[2]);
            return hours*HOUR_IN_SEC + minutes*MIN_IN_SEC + seconds;
        } catch (NumberFormatException e) {
            throw new GameInitException(e.getMessage());
        }
    }
}
