package com.szachmaty.gamelogicservice.infrastructure.controller;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
class GameControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void createGame() throws Exception {
//        GameInitReq gameInitReq =
//                new GameInitReq("FRIEND", "0:15:0", "user1", "user2", "WHITE");
//        ObjectMapper objectMapper = new ObjectMapper();
//        mvc.perform(post(GAME_INIT)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
    }
}