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
        CompletableFuture<Boolean> connected = mqttClientConnection.connect();

        try {
            boolean sessionPresent = connected.get();
            System.out.println("Connected to " + (!sessionPresent ? "new" : "existing") + " session!");
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        try {
            CompletableFuture<Integer> published = mqttClientConnection.publish(new MqttMessage(topic, message.getBytes(), QualityOfService.AT_LEAST_ONCE, false));
            published.get();
            Thread.sleep(1000);

            CompletableFuture<Void> disconnected = mqttClientConnection.disconnect();
            disconnected.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Complete!");
    }
}