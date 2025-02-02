package ch.martinelli.oss.tombola.boundary;

import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlForm;
import org.htmlunit.html.HtmlHeading1;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlTable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
class TombolasControllerTest {

	@Autowired
	private MockMvc mockMvc;

	private WebClient webClient;

	@BeforeEach
	void setup() {
		// Build WebClient with MockMvc
		webClient = MockMvcWebClientBuilder.mockMvcSetup(mockMvc).build();

		// Configure WebClient
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setJavaScriptEnabled(true);
	}

	@AfterEach
	void teardown() {
		if (webClient != null) {
			webClient.close();
		}
	}

	@WithMockUser
	@Test
	void create_tombola() throws Exception {
		// Navigate to the tombola list
		HtmlPage tombolasPage = webClient.getPage("http://localhost/tombolas");

		// Check if the table is empty
		HtmlTable tombolasTable = tombolasPage.getFirstByXPath("//table");
		assertThat(tombolasTable.getRows()).hasSize(1); // Header Row

		// Create a new tombola
		HtmlPage tombolaPage = tombolasPage.getAnchorByHref("/tombolas/new").click();
		assertThat(tombolaPage.getTitleText()).isEqualTo("Tombola");

		HtmlForm tombolaForm = tombolaPage.getFormByName("tombola-form");
		tombolaForm.getInputByName("name").setValueAttribute("Test Tombola");
		tombolaPage = tombolaForm.getButtonByName("save").click();

		// Check if tombola was saved
		HtmlHeading1 h1 = tombolaPage.getFirstByXPath("//h1");
		assertThat(h1.getTextContent()).isEqualTo("Test Tombola");

		// Navigate to the tombola list
		tombolasPage = tombolaPage.getAnchorByHref("/").click();

		// Check if tombola is displayed in the table
		tombolasTable = tombolasPage.getFirstByXPath("//table");
		assertThat(tombolasTable.getRows()).hasSize(2);

		// Go to the prize form
		HtmlPage prizesPage = tombolasPage.getAnchorByHref("/tombolas/1/select").click();
		assertThat(prizesPage.getTitleText()).isEqualTo("Tombola");

		// Check if the prize table is empty
		HtmlTable prizesTable = prizesPage.getFirstByXPath("//table");
		assertThat(prizesTable.getRows()).hasSize(1); // Header Row

		// Check if correct tombola is displayed
		h1 = prizesPage.getFirstByXPath("//h1");
		assertThat(h1.getTextContent()).isEqualTo("Test Tombola");

		HtmlForm prizesForm = prizesPage.getFormByName("prizes-form");
		prizesForm.getInputByName("number").setValueAttribute("1");
		prizesForm.getInputByName("name").setValueAttribute("Maze");
		prizesPage = prizesForm.getButtonByName("save").click();

		// Check if the prize table is empty
		prizesTable = prizesPage.getFirstByXPath("//table");
		assertThat(prizesTable.getRows()).hasSize(2);

		// Update prize
		HtmlPage prizePage = prizesPage.getAnchorByHref("/prizes/1").click();

		// Check name
		h1 = prizePage.getFirstByXPath("//h1");
		assertThat(h1.getTextContent()).isEqualTo("Maze");

		HtmlForm prizeForm = prizePage.getFormByName("prize-form");
		prizeForm.getInputByName("number").setValueAttribute("1");
		prizeForm.getInputByName("name").setValueAttribute("Puzzle");
		prizePage = prizeForm.getButtonByName("save").click();

		h1 = prizePage.getFirstByXPath("//h1");
		assertThat(h1.getTextContent()).isEqualTo("Puzzle");
	}

}