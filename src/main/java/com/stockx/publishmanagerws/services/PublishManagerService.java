package com.stockx.publishmanagerws.services;

import com.stockx.publishmanagerws.adapters.MqttAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PublishManagerService {

    @Autowired
    MqttAdapter mqttAdapter;

    public void publishMessage(String topic, String message) {
        mqttAdapter.publish(topic, message);
    }


}
