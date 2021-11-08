package com.stockx.publishmanagerws.controllers;

import com.stockx.publishmanagerws.services.PublishManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublishManagerController {

    @Autowired
    private PublishManagerService publishManagerService;

    @PostMapping(value = "/publish")
    public void publishStockListing(@RequestParam("ticker") String topic, String message) {
        publishManagerService.publishStockListing(topic, message);
    }
}
