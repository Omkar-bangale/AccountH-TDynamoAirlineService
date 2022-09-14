package com.dynamoairlineservice.app.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import com.dynamoairlineservice.app.model.Airline;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class DynamoConfig {
	

	@Value("${amazon.dynamodb.endpoint}")
	private String dynamodbEndpoint;
	@Value("${amazon.dynamodb.region}")
	private String dynamodbRegion;
	

	@Bean
	public DynamoDbClient getDynamoDbClient() {
		
		DynamoDbClient client = DynamoDbClient
				.builder()
				.endpointOverride(URI.create(dynamodbEndpoint))
				.region(Region.of(dynamodbRegion))
				.credentialsProvider(StaticCredentialsProvider.create(
						AwsBasicCredentials.create("AKIASDMVW4ECGIAFAMKG", "s5sQCeBSQwxXTC3hySFot+XlONQdFKWYr6rvl8Fi")))
				.build();
		return client;
	}
	
	@Bean
	public DynamoDbEnhancedClient getDynamoDbEnhancedClient() {
		
		
		DynamoDbEnhancedClient enhancedClient = 
			    DynamoDbEnhancedClient.builder()
                  .dynamoDbClient(getDynamoDbClient())
                  .build();
		return enhancedClient;
	}
	
	 @Bean
	    public DynamoDbTable<Airline> getDynamoDbTable(){
	        String tableName = "Airline";
	        DynamoDbTable<Airline> table = getDynamoDbEnhancedClient().table(tableName, TableSchema.fromBean(Airline.class));
	        return table;
	    }
	

}
