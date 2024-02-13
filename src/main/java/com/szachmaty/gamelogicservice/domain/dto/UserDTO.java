package com.szachmaty.gamelogicservice.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDTO {
    private String username;
    public UserDTO(String username) {
        this.username = username;
    }
    public UserDTO() {}
}
