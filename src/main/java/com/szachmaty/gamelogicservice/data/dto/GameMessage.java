package com.szachmaty.gamelogicservice.data.dto;

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
