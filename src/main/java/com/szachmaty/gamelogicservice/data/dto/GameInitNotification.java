package com.szachmaty.gamelogicservice.data.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameInitNotification {
    private String inviteSenderId;
    private String inviteReceiverId;
    private String gameCode;
}
