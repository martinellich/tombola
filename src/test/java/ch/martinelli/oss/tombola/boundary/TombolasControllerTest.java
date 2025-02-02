package ch.martinelli.oss.tombola.boundary;

import org.htmlunit.html.*;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.Assertions.assertThat;

class TombolasControllerTest extends ControllerTest {

	@WithMockUser
	@Test
	void create_tombola() throws Exception {
		// Navigate to the tombola list
		HtmlPage tombolasPage = webClient.getPage("http://localhost/tombolas");

		// Check if the table is empty
		HtmlTable tombolasTable = tombolasPage.getFirstByXPath("//table");
		assertThat(tombolasTable.getRows()).hasSize(2); // Header Row incl.

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
		assertThat(tombolasTable.getRows()).hasSize(3);

		// Go to the prize form
		HtmlPage prizesPage = tombolasPage.getAnchorByHref("/tombolas/1/select").click();
		assertThat(prizesPage.getTitleText()).isEqualTo("Tombola");

		// Check if the prize table is empty
		HtmlTable prizesTable = prizesPage.getFirstByXPath("//table");
		assertThat(prizesTable.getRows()).hasSize(1);

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
		HtmlPage prizePage = prizesPage.getAnchorByHref("/prizes/2").click();

		// Check name
		h1 = prizePage.getFirstByXPath("//h1");
		assertThat(h1.getTextContent()).isEqualTo("Maze");

		HtmlForm prizeForm = prizePage.getFormByName("prize-form");
		prizeForm.getInputByName("number").setValueAttribute("1");
		prizeForm.getInputByName("name").setValueAttribute("Puzzle");
		prizePage = prizeForm.getButtonByName("save").click();

		h1 = prizePage.getFirstByXPath("//h1");
		assertThat(h1.getTextContent()).isEqualTo("Puzzle");

		// Navigate to the created tombola
		tombolaPage = webClient.getPage("http://localhost/tombolas/1");

		// Check name
		h1 = tombolaPage.getFirstByXPath("//h1");
		assertThat(h1.getTextContent()).isEqualTo("Test Tombola");
	}

	@WithMockUser
	@Test
	void tombola_not_found() throws Exception {
		// Navigate to non existing tombola
		HtmlPage tombolaPage = webClient.getPage("http://localhost/tombolas/999");

		// Check the error message
		DomElement message = tombolaPage.getElementById("message");
		assertThat(message.getTextContent()).contains("Die Tombola 999 ist nicht vorhanden");
	}

}