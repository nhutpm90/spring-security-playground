package com.example.security.api;

import java.util.Date;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

	@GetMapping("/live-check")
	public String liveCheck() throws Exception {
		return "liveCheck:: " + new Date();
	}
}
