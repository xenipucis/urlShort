package es.urlSh.controllers;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WelcomeController {

	private String message = "Hello, World!!!";
	
	@RequestMapping("/")
	public String welcome(Map<String, Object> model) {
		
		return "welcome";
	}
	
}
