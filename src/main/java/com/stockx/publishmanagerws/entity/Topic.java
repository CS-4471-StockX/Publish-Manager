package com.stockx.publishmanagerws.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "Topics")
public class Topic {
    @DynamoDBHashKey
    private String symbol;

    @DynamoDBAttribute
    private String service;

    @DynamoDBAttribute
    private int numOfSubscribers;
}