package com.szachmaty.gamelogicservice.infrastructure.controller.ws;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameMessage {
    private String userId;
    private String gameCode;
    private String move;
    private boolean isAIMode;
}
