package es.urlSh.controllers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

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
	public String register() {
		return "register";
	}
	
	@RequestMapping(value="/register", method=RequestMethod.POST) 
	public String saveURL(String url, Model model) {
		model.addAttribute("url", url);
		final UrlValidator urlValidator = new UrlValidator(new String[]{"http", "https", "ftp"});
		if (urlValidator.isValid(url)) {
			final String id = Hashing.murmur3_32().hashString(url, StandardCharsets.UTF_8).toString();
			model.addAttribute("url_short", id);
			redis.opsForValue().set(id, url);
			return "success";
		} else {
			return "errorRegister";
		}
	}
	
	@RequestMapping(value="/browse")
	public String addUrlForBrowsing() {
		return "browse";
	}
	
	@RequestMapping(value="/browse", method=RequestMethod.POST) 
	public void browseURL(String url, HttpServletResponse resp) throws IOException {
		final String url_long = redis.opsForValue().get(url);
		if (url_long != null)
			resp.sendRedirect(url_long);
		else
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
	}
}
