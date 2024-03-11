package com.szachmaty.gamelogicservice.infrastructure.controller.ws;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameMessage {
    private String userId;
    private String gameCode;
    private String move;
}
