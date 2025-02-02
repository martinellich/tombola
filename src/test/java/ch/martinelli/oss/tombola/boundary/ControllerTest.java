package ch.martinelli.oss.tombola.boundary;

import org.htmlunit.WebClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;

import java.util.Locale;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    protected WebClient webClient;

    @BeforeEach
    void setup() {
        // Build WebClient with MockMvc
        webClient = MockMvcWebClientBuilder.mockMvcSetup(mockMvc).build();

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
