package ch.martinelli.oss.tombola.boundary;

import org.htmlunit.html.*;
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
	void search_prize_with_empty_term() throws IOException {
		// Navigate to non existing tombola
		HtmlPage tombolaPage = webClient.getPage("http://localhost/tombolas/9999/select");

		HtmlForm searchForm = tombolaPage.getFormByName("search-form");
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
	void try_to_create_price_with_same_number() throws IOException {
		// Navigate to the prizes' page without a tombola in the session
		// Expect redirect to the tombolas' page
		HtmlPage prizesPage = webClient.getPage("http://localhost/tombolas/9999/select");

		// Check if the prize table has one prize
		HtmlTable prizesTable = prizesPage.getFirstByXPath("//table");
		assertThat(prizesTable.getRows()).hasSize(2);

		// Check if the correct tombola is displayed
		HtmlHeading1 h1 = prizesPage.getFirstByXPath("//h1");
		assertThat(h1.getTextContent()).isEqualTo("Test");

		// Try to add prize with existing number
		HtmlForm prizesForm = prizesPage.getFormByName("prizes-form");
		prizesForm.getInputByName("number").setValueAttribute("9999");
		prizesForm.getInputByName("name").setValueAttribute("Test");
		prizesPage = prizesForm.getButtonByName("save").click();

		// Check the error message
		DomElement message = prizesPage.getElementById("message");
		assertThat(message.getTextContent()).contains("Die Nummer 9.999 existiert bereits bei Preis Test");

		// Check if the prize table still has one price
		prizesTable = prizesPage.getFirstByXPath("//table");
		assertThat(prizesTable.getRows()).hasSize(2);
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