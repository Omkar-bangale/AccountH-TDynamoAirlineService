package com.dynamoairlineservice.app.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.dynamoairlineservice.app.model.Airline;
import com.dynamoairlineservice.app.repository.Airlinerepo;

@RestController
@RequestMapping("/ariline")
public class DynamoAirlineController {
	
	@Autowired
	private Airlinerepo airlinerepo;
	

	@PostMapping("/add")
	public String addAirline(@RequestHeader Map<String, String> headers,@RequestBody Airline airline)
	{
		DynamoAirlineController.validateToken(headers);
		if(validated) {
		if(airlinerepo.addAirline(airline))
			return "Airline added successfully";
		else
			return "Error in adding airline";
		}else
			return "Token Validation Failed";
	}
	
	@GetMapping("/status/{airlineName}")
	public boolean getAirlineStatus(@PathVariable String airlineName)
	{
		return airlinerepo.getAirlineStatus(airlineName);
	}
	
	@GetMapping("/all")
	public List<Airline> getAllAirlines(@RequestHeader Map<String, String> headers)
	{
		DynamoAirlineController.validateToken(headers);
		if(validated) {
	return airlinerepo.getAllAirline();
		}
		else
			throw new RuntimeException("Error in Token Validation");
	}
	
	@PutMapping("/update/{airlineName}")
	public String updateAirline(@RequestHeader Map<String, String> headers,@RequestBody Airline airline, @PathVariable String airlineName)
	{
		DynamoAirlineController.validateToken(headers);
		if(validated) {
		if(airlinerepo.updateAirlines(airline, airlineName))
			return "Airline Updated successfully";
		else
			return "Error in updating Airline";
		}else
			return "Token Validation Failed";
	}
	
	@DeleteMapping("/delete/{airlineName}")
	public String removeAirline(@RequestHeader Map<String, String> headers,@PathVariable String airlineName)
	{
		DynamoAirlineController.validateToken(headers);
		if(validated) {
		if(airlinerepo.removeAirline(airlineName))
			return "Airline Deleted successfully";
		
		else
			return "error in deleting the airline"; 
		}
		else
			return "Token validation failed";
	}
	private static boolean validated=false;
	
	
	public  static void validateToken (Map<String, String> header)
	{
	
		String token="";
		for(String key : header.keySet())
		{
			if(key.equals("authorization"))
				token=header.get(key);
		}
		HttpHeaders httpheader = new HttpHeaders();
		httpheader.set("Authorization", token);
		HttpEntity<Void> requestentity = new HttpEntity<>(httpheader);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Boolean> response = restTemplate.exchange("http://34.216.12.139:9004/validatejwt",HttpMethod.GET, requestentity,boolean.class);
		validated=response.getBody().booleanValue();
	}
}
