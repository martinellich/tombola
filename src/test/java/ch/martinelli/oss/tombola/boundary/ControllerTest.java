package ch.martinelli.oss.tombola.boundary;

import org.htmlunit.WebClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.web.context.WebApplicationContext;

import java.util.Locale;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class ControllerTest {

	@Autowired
	private WebApplicationContext context;

	protected WebClient webClient;

	@BeforeEach
	void setup() {
		// Build WebClient with MockMvc and Spring Security
		webClient = MockMvcWebClientBuilder.webAppContextSetup(context, springSecurity()).build();

		// Configure WebClient
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setJavaScriptEnabled(true);

		Locale.setDefault(Locale.GERMAN);
	}

	@AfterEach
	void teardown() {
		if (webClient != null) {
			webClient.close();
		}
	}

}
