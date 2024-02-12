package com.szachmaty.gamelogicservice.infrastructure.controller.data;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameCheckPlayerReq {
    private String oponent;
    private String gameCode;
}
