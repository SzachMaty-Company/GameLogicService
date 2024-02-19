package com.szachmaty.gamelogicservice.application.gameinit;


import java.time.LocalTime;
import java.util.UUID;

public final class GameInitUtil {

    /**
     *
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
    public static LocalTime gameTimeParser(String gameTime) {
        String[] parts = gameTime.split(":");
        if(parts[0].equals("0")) {
            return LocalTime.of(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
        } else {
            return LocalTime.of(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
        }
    }
}
