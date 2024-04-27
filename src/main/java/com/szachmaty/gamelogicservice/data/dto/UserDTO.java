package com.szachmaty.gamelogicservice.data.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private String whiteUserId;
    private String blackUserId;
}
