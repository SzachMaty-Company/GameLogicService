package com.szachmaty.gamelogicservice.domain.dto;

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
