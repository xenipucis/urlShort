package es.urlSh.controllers;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.hash.Hashing;

@Controller
public class URLController {

	@Autowired
	private StringRedisTemplate redis;
	
	@RequestMapping(value="/register", method=RequestMethod.GET) 
	public String register(Map<String, Object> model) {
		
		return "register";
	}
	
	@RequestMapping(value="/register", method=RequestMethod.POST) 
	public String saveURL(String url, Model model) {
		model.addAttribute("url", url);
		final UrlValidator urlValidator = new UrlValidator(new String[]{"http", "https", "ftp"});
		if (urlValidator.isValid(url)) {
			final String id = Hashing.murmur3_32().hashString(url, StandardCharsets.UTF_8).toString();
			model.addAttribute("url_short", id);
			return "success";
		} else {
			return "errorRegister";
		}
	}
}
