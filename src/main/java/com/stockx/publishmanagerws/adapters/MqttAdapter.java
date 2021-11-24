package com.stockx.publishmanagerws.adapters;

import lombok.AllArgsConstructor;
import software.amazon.awssdk.crt.mqtt.MqttClientConnection;
import software.amazon.awssdk.crt.mqtt.MqttMessage;
import software.amazon.awssdk.crt.mqtt.QualityOfService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@AllArgsConstructor
public class MqttAdapter {

    private MqttClientConnection mqttClientConnection;

    public void publish(String topic, String message) {
        try {
            CompletableFuture<Integer> published = mqttClientConnection.publish(new MqttMessage(topic, message.getBytes(), QualityOfService.AT_LEAST_ONCE, false));
            published.get();
            Thread.sleep(1000);
            System.out.println("Message for topic: " + topic + " published successfully!");
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}