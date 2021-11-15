package com.stockx.publishmanagerws.configurations;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.stockx.publishmanagerws.adapters.LiveStockTrackerAdapter;
import com.stockx.publishmanagerws.adapters.MarketIndexTrackerAdapter;
import com.stockx.publishmanagerws.adapters.MqttAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import software.amazon.awssdk.crt.io.ClientBootstrap;
import software.amazon.awssdk.crt.io.EventLoopGroup;
import software.amazon.awssdk.crt.io.HostResolver;
import software.amazon.awssdk.crt.mqtt.MqttClientConnection;
import software.amazon.awssdk.iot.AwsIotMqttConnectionBuilder;

import java.util.UUID;

@Configuration
public class AppConfiguration {
    @Value("${mqtt.client.rootCaPath}")
    private String mqttRootCaPath;

    @Value("${mqtt.client.certPath}")
    private String mqttCertPath;

    @Value("${mqtt.client.keyPath}")
    private String mqttKeyPath;

    @Value("${mqtt.client.endpoint}")
    private String mqttEndpoint;

    @Value("${amazon.dynamodb.endpoint}")
    private String amazonDynamoDBEndpoint;

    @Value("${amazon.aws.accesskey}")
    private String amazonAWSAccessKey;

    @Value("${amazon.aws.secretkey}")
    private String amazonAWSSecretKey;

    private static final int mqttPort = 8883;

    @Bean
    public MqttClientConnection mqttClientConnection() {
        String clientId = "test-" + UUID.randomUUID();

        EventLoopGroup eventLoopGroup = new EventLoopGroup(1);
        HostResolver resolver = new HostResolver(eventLoopGroup);
        ClientBootstrap clientBootstrap = new ClientBootstrap(eventLoopGroup, resolver);
        AwsIotMqttConnectionBuilder builder = AwsIotMqttConnectionBuilder.newMtlsBuilderFromPath(mqttCertPath, mqttKeyPath);
        builder.withCertificateAuthorityFromPath(null, mqttRootCaPath);

        builder.withBootstrap(clientBootstrap)
                .withClientId(clientId)
                .withEndpoint(mqttEndpoint)
                .withPort((short) mqttPort)
                .withCleanSession(true)
                .withProtocolOperationTimeoutMs(60000);
        return builder.build();
    }

    @Bean
    public MqttAdapter mqttAdapter(MqttClientConnection mqttClientConnection) {
        return new MqttAdapter(mqttClientConnection);
    }


    @Bean
    public DynamoDBMapper dynamoDBMapper() {
        return new DynamoDBMapper(buildAmazonDynamoDB());
    }

    @Bean
    public LiveStockTrackerAdapter liveStockTrackerAdapter(){
        return new LiveStockTrackerAdapter();
    }

    @Bean
    public MarketIndexTrackerAdapter marketIndexTrackerAdapter(){
        return new MarketIndexTrackerAdapter();
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    private AmazonDynamoDB buildAmazonDynamoDB() {
        return AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(amazonDynamoDBEndpoint, "us-east-2")).withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey))).build();
    }

}
