package com.szachmaty.gamelogicservice.infrastructure.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SzachController {
    @GetMapping("/api")
    public String testApi() {
        return "Docker is working!";
    }
}
