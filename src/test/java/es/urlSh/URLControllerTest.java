package es.urlSh;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import es.urlSh.controllers.URLController;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = UrlShApplication.class)
@WebAppConfiguration
public class URLControllerTest {

	@Autowired
	public StringRedisTemplate redis;
	
	@Test
	public void testRedisTemplate() {
		assertNotNull(redis);
	}
	
	@Test
	public void testRegisterPage() throws Exception {
		URLController controller = new URLController();
		MockMvc mockMvc = standaloneSetup(controller).build();
		mockMvc.perform(get("/register"))
				.andExpect(view().name("registerForm"));
	}
	
	@Test
	public void testShoudFailSaveInvalidUrl() throws Exception {
		String invalidUrl = "file://unsupported/protocol";
		URLController controller = new URLController();
		MockMvc mockMvc = standaloneSetup(controller).build();
		mockMvc.perform(post("/register")
		           .param("url", invalidUrl))
		           .andExpect(view().name("errorRegister"));
	}
	
	@Test
	public void testShoudSaveValidUrl() throws Exception {
		String validUrl = "https://www.google.ro/?gws_rd=ssl#q=url+javascript";
		URLController controller = new URLController(redis);
		MockMvc mockMvc = standaloneSetup(controller).build();
		mockMvc.perform(post("/register")
		           .param("url", validUrl))
		           .andExpect(view().name("success"));
	}
	
	@Test
	public void testPassValidRedisIdForUrl() throws Exception {
		String validUrl = "https://www.google.ro/?gws_rd=ssl#q=url+javascript";
		URLController controller = new URLController(redis);
		MockMvc mockMvc = standaloneSetup(controller).build();
		mockMvc.perform(post("/register")
		           .param("url", validUrl))
		           .andExpect(model().attribute("url_short", "a21a464d"));
	}
	
	@Test
	public void testBrowsePage() throws Exception {
		URLController controller = new URLController();
		MockMvc mockMvc = standaloneSetup(controller).build();
		mockMvc.perform(get("/browse"))
				.andExpect(view().name("browseForm"));
	}
	
	@Test
	public void testRedirect() throws Exception {
		testShoudSaveValidUrl();
		String validShortUrl = "a21a464d";
		URLController controller = new URLController(redis);
		String expectedUrl = redis.opsForValue().get(validShortUrl);
		
		MockMvc mockMvc = standaloneSetup(controller).build();
		mockMvc.perform(post("/browse")
				.param("url", validShortUrl))
				.andExpect(redirectedUrl(expectedUrl))
				;
	}
	

}
