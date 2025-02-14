package dev.jbaby;

import static org.apache.wicket.feedback.IFeedbackMessageFilter.ALL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class PersonalInformationPageTest {

	@Autowired
	private WebApplication application;

	@Autowired
	private UserRepository userRepository;

	private WicketTester tester;

	@BeforeEach
	void setUp() {

		tester = new WicketTester(application);
		userRepository.deleteAll();

	}

	@Test
	void rendersSuccessfully() {

		// given & when
		tester.startPage(PersonalInformationPage.class);

		// then
		tester.assertRenderedPage(PersonalInformationPage.class);
		tester.assertInvisible("form:success");
		tester.assertInvisible("form:errors");

	}

	@Test
	void givenPageRenderedSuccessfully_whenSubmitWithNoValuesProvided_thenErrorsAreShown() {

		// given
		tester.startPage(PersonalInformationPage.class);
		FormTester formTester = tester.newFormTester("form");

		// when
		formTester.submit();

		// then
		tester.assertInvisible("form:success");
		tester.assertVisible("form:errors");
		var firstName = tester.getComponentFromLastRenderedPage("form:firstName");
		var lastName = tester.getComponentFromLastRenderedPage("form:lastName");
		assertEquals(1, firstName.getFeedbackMessages().size());
		assertEquals(1, lastName.getFeedbackMessages().size());
		assertEquals(3, tester.getFeedbackMessages(ALL).size());

		assertEquals(0, userRepository.count());

	}

	@Test
	void givenPageRenderedSuccessfully_whenSubmitWithFirstAndLastName_thenSuccess() {

		// given
		tester.startPage(PersonalInformationPage.class);
		FormTester formTester = tester.newFormTester("form");

		// when
		formTester.setValue("firstName", "Joe");
		formTester.setValue("lastName", "Johnson");
		formTester.submit();

		// then
		tester.assertVisible("form:success");
		tester.assertInvisible("form:errors");
		assertEquals(1, tester.getFeedbackMessages(FeedbackMessage::isInfo).size());
		assertEquals(
				"User information saved successfully!",
				tester.getFeedbackMessages(FeedbackMessage::isInfo).getFirst().getMessage());
		assertTrue(tester.getFeedbackMessages(FeedbackMessage::isError).isEmpty());

		List<User> users = userRepository.findAll();
		assertEquals(1, users.size());
		User user = users.getFirst();
		assertEquals("Joe", user.getFirstName());
		assertEquals("Johnson", user.getLastName());
		assertNull(user.getEmail());
		assertNull(user.getStreetAddress());
		assertNull(user.getCity());
		assertNull(user.getPostalCode());
		assertNull(user.getCountry());
		assertNull(user.getRegion());

	}

	@Test
	void givenPageRenderedSuccessfully_whenSubmitWithAllDetails_thenSuccess() {

		// given
		tester.startPage(PersonalInformationPage.class);
		FormTester formTester = tester.newFormTester("form");

		// when
		formTester.setValue("firstName", "Joe");
		formTester.setValue("lastName", "Johnson");
		formTester.setValue("email", "jj@mail.com");
		formTester.select("country", 10);
		formTester.setValue("streetAddress", "E Street 1974");
		formTester.setValue("city", "Asbury Park");
		formTester.select("region", 6);
		formTester.setValue("postalCode", "07712");
		formTester.submit();

		// then
		tester.assertVisible("form:success");
		tester.assertInvisible("form:errors");
		assertEquals(1, tester.getFeedbackMessages(FeedbackMessage::isInfo).size());
		assertEquals(
				"User information saved successfully!",
				tester.getFeedbackMessages(FeedbackMessage::isInfo).getFirst().getMessage());
		assertTrue(tester.getFeedbackMessages(FeedbackMessage::isError).isEmpty());

		List<User> users = userRepository.findAll();
		assertEquals(1, users.size());
		User user = users.getFirst();
		assertEquals("Joe", user.getFirstName());
		assertEquals("Johnson", user.getLastName());
		assertEquals("jj@mail.com", user.getEmail());
		assertEquals("E Street 1974", user.getStreetAddress());
		assertEquals("Asbury Park", user.getCity());
		assertEquals("07712", user.getPostalCode());
		assertEquals("United States", user.getCountry());
		assertEquals("New Jersey", user.getRegion());

	}
}