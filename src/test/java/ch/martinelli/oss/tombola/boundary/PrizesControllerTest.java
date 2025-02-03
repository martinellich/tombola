package ch.martinelli.oss.tombola.boundary;

import org.htmlunit.html.HtmlForm;
import org.htmlunit.html.HtmlHeading1;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlTable;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class PrizesControllerTest extends ControllerTest {

	@WithMockUser
	@Test
	void search_prize() throws IOException {
		// Navigate to non existing tombola
		HtmlPage tombolaPage = webClient.getPage("http://localhost/tombolas/9999/select");

		HtmlForm searchForm = tombolaPage.getFormByName("search-form");
		searchForm.getInputByName("searchTerm").setValueAttribute("Test");
		tombolaPage = searchForm.getButtonByName("search").click();

		// Check if the prize table contains the added tombola
		HtmlTable prizesTable = tombolaPage.getFirstByXPath("//table");
		assertThat(prizesTable.getRows()).hasSize(2);

		// Search for non-existing prize number
		searchForm = tombolaPage.getFormByName("search-form");
		searchForm.getInputByName("searchTerm").setValueAttribute("999");
		tombolaPage = searchForm.getButtonByName("search").click();

		// Check if the prize table is empty
		prizesTable = tombolaPage.getFirstByXPath("//table");
		assertThat(prizesTable.getRows()).hasSize(1);
	}

	@WithMockUser
	@Test
	void no_tombola_in_session() throws IOException {
		// Navigate to the prizes' page without a tombola in the session
		// Expect redirect to the tombolas' page
		HtmlPage tombolasPage = webClient.getPage("http://localhost/prizes");

		// Check name
		HtmlHeading1 h1 = tombolasPage.getFirstByXPath("//h1");
		assertThat(h1.getTextContent()).isEqualTo("Tombolas");
	}

}