package com.stockx.publishmanagerws.mqtt;

import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.crt.CRT;
import software.amazon.awssdk.crt.CrtRuntimeException;
import software.amazon.awssdk.crt.io.*;
import software.amazon.awssdk.crt.mqtt.MqttClientConnection;
import software.amazon.awssdk.crt.mqtt.MqttClientConnectionEvents;
import software.amazon.awssdk.crt.mqtt.MqttMessage;
import software.amazon.awssdk.crt.mqtt.QualityOfService;
import software.amazon.awssdk.iot.AwsIotMqttConnectionBuilder;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ClientConnection {

    @Value("${mqtt.client.endpoint}")
    private String endpoint;
    @Value("${mqtt.client.region}")
    private String region;
    @Value("${mqtt.client.certPath}")
    private String certPath;
    @Value("${mqtt.client.keyPath}")
    private String keyPath;

    private MqttClientConnection connection;

    private static final int PORT = 8883;
    private static final String CLIENT_ID = "test-" + UUID.randomUUID();


    public ClientConnection() {
        establishConnection();
    }

    public void publish(String topic, String message) {
        CompletableFuture<Integer> published = connection.publish(new MqttMessage(topic, message.getBytes(), QualityOfService.AT_LEAST_ONCE, false));
        try {
            published.get();
            Thread.sleep(1000);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void establishConnection() {
        MqttClientConnectionEvents callbacks = new MqttClientConnectionEvents() {
            @Override
            public void onConnectionInterrupted(int errorCode) {
                if (errorCode != 0) {
                    System.out.println("Connection interrupted: " + errorCode + ": " + CRT.awsErrorString(errorCode));
                }
            }

            @Override
            public void onConnectionResumed(boolean sessionPresent) {
                System.out.println("Connection resumed: " + (sessionPresent ? "existing session" : "clean session"));
            }
        };

        try (EventLoopGroup eventLoopGroup = new EventLoopGroup(1);
             HostResolver resolver = new HostResolver(eventLoopGroup);
             ClientBootstrap clientBootstrap = new ClientBootstrap(eventLoopGroup, resolver);
             AwsIotMqttConnectionBuilder builder = AwsIotMqttConnectionBuilder.newMtlsBuilderFromPath(certPath, keyPath)) {
            builder.withBootstrap(clientBootstrap)
                    .withConnectionEventCallbacks(callbacks)
                    .withClientId(CLIENT_ID)
                    .withEndpoint(endpoint)
                    .withPort((short) PORT)
                    .withCleanSession(true)
                    .withProtocolOperationTimeoutMs(60000);

            builder.withWebsockets(true);
            builder.withWebsocketSigningRegion(region);

            connection = builder.build();
            try {

                CompletableFuture<Boolean> connected = connection.connect();
                try {
                    boolean sessionPresent = connected.get();
                    System.out.println("Connected to " + (!sessionPresent ? "new" : "existing") + " session!");
                } catch (Exception ex) {
                    throw new RuntimeException("Exception occurred during connect", ex);
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        } catch (CrtRuntimeException ex) {
            ex.printStackTrace();
        }
    }
}
