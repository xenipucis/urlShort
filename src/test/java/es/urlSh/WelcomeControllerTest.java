package es.urlSh;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import es.urlSh.controllers.WelcomeController;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = UrlShApplication.class)
@WebAppConfiguration
public class WelcomeControllerTest {

	@Test
	public void testWelcomePage() throws Exception {
		WelcomeController controller = new WelcomeController();
		MockMvc mockMvc = standaloneSetup(controller).build();
		mockMvc.perform(get("/"))
				.andExpect(view().name("welcome"));
	}

}
