package com.stockx.publishmanagerws.controllers;

import com.stockx.publishmanagerws.services.PublishManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PublishManagerController {

    @Autowired
    private PublishManagerService publishManagerService;

    @PostMapping(value = "/publish")
    public void publishMessage(@RequestParam("topic") String topic, @RequestBody String message) {
        publishManagerService.publishMessage(topic, message);
    }
}
