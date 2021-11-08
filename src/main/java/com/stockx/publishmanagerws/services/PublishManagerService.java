package com.stockx.publishmanagerws.services;

import com.stockx.publishmanagerws.Stock;
import com.stockx.publishmanagerws.mqtt.ClientConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PublishManagerService {

    @Autowired
    private ClientConnection clientConnection;

    public void publishStockListing(String topic, String message) {
        clientConnection.publish(topic, message);
    }

}
