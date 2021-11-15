package com.stockx.publishmanagerws.repository;
import java.util.*;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.stockx.publishmanagerws.entity.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class TopicRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public Topic getTopicBySymbol(String symbol) {
        return dynamoDBMapper.load(Topic.class, symbol);
    }

    public List<Topic> getTopicByService(String service){
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":val1", new AttributeValue().withS(service));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("service = :val1").withExpressionAttributeValues(eav);

        List<Topic> scanResult = dynamoDBMapper.scan(Topic.class, scanExpression);

        return scanResult;
    }

}