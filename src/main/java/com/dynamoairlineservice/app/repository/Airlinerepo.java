package com.dynamoairlineservice.app.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.dynamoairlineservice.app.model.Airline;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

@Service
public class Airlinerepo {
	
	@Autowired
	private DynamoDbEnhancedClient enhancedClient;
	
	 @Autowired
	    private DynamoDbTable<Airline> airlineTable;
	 
	 public boolean addAirline(Airline airline) {
		 
		 airlineTable.putItem(airline);
		 return true;
	 }

	public boolean getAirlineStatus(String airlineName)
	{
		Airline airline1= airlineTable.scan().items().stream().filter(a -> a.getAirlineName().equals(airlineName)).findFirst().get();
		return airline1.getAirlineStatus();
	}

	public List<Airline> getAllAirline()
	{
		return (List<Airline>) airlineTable.scan().items().stream().collect(Collectors.toList());
	}
	
	public boolean updateAirlines(Airline airline1, String airlineName)
	{
		Airline airline =airlineTable.scan().items().stream().filter(a -> a.getAirlineName().equals(airlineName)).findFirst().get();
		airline.setAirlineAddress(airline1.getAirlineAddress());
		airline.setAirlineContact(airline1.getAirlineContact());
		airline.setAirlineStatus(airline1.getAirlineStatus());
		
		airlineTable.putItem(airline);
		return true;
	}
	
	public boolean removeAirline(String airlineName)
	{
		Airline airline = airlineTable.scan().items().stream().filter(a-> a.getAirlineName().equals(airlineName)).findFirst().get();
		
		airlineTable.deleteItem(airline);
		return true;
	}
}

