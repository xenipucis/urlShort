package es.urlSh.controllers;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class URLController {

	
	
	@RequestMapping(value="/register", method=RequestMethod.GET) 
	public String register(Map<String, Object> model) {
		
		return "register";
	}
	
	@RequestMapping(value="/register", method=RequestMethod.POST) 
	public String saveURL(String url, Model model) {
		model.addAttribute("url", url);
		return "success";
	}
}
