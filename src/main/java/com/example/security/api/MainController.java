package com.example.security.api;

import java.util.Date;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

	@GetMapping("/live-check")
	public String liveCheck() throws Exception {
		return "liveCheck:: " + new Date();
	}
	
	@PostMapping("/test-post-request")
	public String testPostRequest() throws Exception {
		return "testPostRequest:: " + new Date();
	}
	
	@GetMapping("/test-post-request01")
	public String testPostRequest01() throws Exception {
		return "testPostRequest01:: " + new Date();
	}
	
	@PostMapping("/test-post-request02")
	public String testPostRequest02() throws Exception {
		return "testPostRequest02:: " + new Date();
	}
}
