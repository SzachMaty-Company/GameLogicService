package com.szachmaty.gamelogicservice.service.gameinit;


import java.time.Instant;
import java.util.UUID;

public final class GameInitUtil {

    private final static String DATE_TIME_FORMAT = "dd-MM-yyyy HH:mm:ss";
    private final static int SECONDS = 60;

    /**
     * @return uuid
     */
    public static String generateGameCode() {
        final String uuid = UUID.randomUUID().toString();
        return uuid.replace("-" , "").substring(0, 12);
    }

    /**
     * @param gameTime
     * @return GameDuration in seconds
     */
    public static Long gameTimeParser(Integer gameTime) {
        return Long.valueOf(gameTime*SECONDS);
    }

    /**
     * @return Current datetime in format: dd-MM-yyyy HH:mm:ss
     */
    public static String gameStartTimeCreator() {
//        LocalDateTime now = LocalDateTime.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
//        return now.format(formatter);
        return Instant.now().toString();
    }

}
