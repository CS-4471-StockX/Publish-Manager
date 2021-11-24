package com.stockx.publishmanagerws.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublishManagerController {
    @GetMapping("/health")
    public String health() {
        return "UP";
    }
}
