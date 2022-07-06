package com.example.security.api;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CardsController {
	
	@GetMapping("/myCards")
	@PreAuthorize("hasRole('ROOT')")
	public String getCardDetails(String input) {
		return "Here are the card details from the DB";
	}

}
