package dev.danvega;

import java.util.stream.Stream;

import org.apache.wicket.bean.validation.PropertyValidator;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.LambdaModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersonalInformationPage extends WebPage {

	private static final Logger LOG = LoggerFactory.getLogger(PersonalInformationPage.class);

	@SpringBean
	private UserRepository repository;

	private Form<User> form;

	@Override
	protected void onInitialize() {

		super.onInitialize();

		form();
		firstName();
		lastName();
		email();
		country();
		streetAddress();
		city();
		region();
		postalCode();

	}

	private void form() {

		form = new Form<>("form", Model.of(new User())) {

			@Override
			protected void onSubmit() {

				LOG.info("Saving User: {}", getModelObject());
				repository.save(getModelObject());
				success("User information saved successfully!");

			}

			@Override
			protected void onError() {

				LOG.info("User Validation failed for: {}", getModelObject());

			}
		};

		success();
		errors();
		add(form);

	}

	private void errors() {
		FeedbackPanel errors =
				new FeedbackPanel(
						"errors",
						msg -> msg.getLevel() == FeedbackMessage.ERROR) {

					@Override
					protected void onConfigure() {
						super.onConfigure();
						setVisible(anyErrorMessage());
					}

				};
		errors.setOutputMarkupPlaceholderTag(true);
		form.add(errors);
	}

	private void success() {

		FeedbackPanel success =
				new FeedbackPanel(
						"success",
						msg -> msg.getLevel() == FeedbackMessage.SUCCESS) {

					@Override
					protected void onConfigure() {
						super.onConfigure();
						setVisible(anyMessage(FeedbackMessage.SUCCESS));
					}

				};
		success.setOutputMarkupPlaceholderTag(true);
		form.add(success);

	}

	private void firstName() {

		var firstName =
				new TextField<>(
						"firstName",
						new PropertyModel<>(form.getModel(), "firstName"));
		firstName.add(new PropertyValidator<>());
		form.add(firstName);

	}

	private void lastName() {

		var lastName =
				new TextField<>(
						"lastName",
						LambdaModel.of(form.getModel(), User::getLastName, User::setLastName));
		lastName.setRequired(true);
		form.add(lastName);

	}

	private void email() {

		EmailTextField email =
				new EmailTextField(
						"email",
						LambdaModel.of(form.getModel(), User::getEmail, User::setEmail));
		form.add(email);

	}

	private void country() {

		var country =
				new DropDownChoice<>(
						"country",
						LambdaModel.of(form.getModel(), User::getCountry, User::setCountry),
						Stream.of(
										"United States",
										"Canada",
										"United Kingdom",
										"France",
										"Germany",
										"Japan",
										"Italy",
										"Spain",
										"Netherlands",
										"Brazil",
										"Australia")
								.sorted()
								.toList());
		form.add(country);

	}

	private void streetAddress() {

		var streetAddress =
				new TextField<>(
						"streetAddress",
						LambdaModel.of(form.getModel(), User::getStreetAddress, User::setStreetAddress));
		form.add(streetAddress);

	}

	private void city() {

		var city = new TextField<>(
				"city",
				LambdaModel.of(form.getModel(), User::getCity, User::setCity));
		form.add(city);

	}

	private void region() {

		var region =
				new DropDownChoice<>(
						"region",
						LambdaModel.of(form.getModel(), User::getRegion, User::setRegion),
						Stream.of(
										"Alaska",
										"Texas",
										"California",
										"Montana",
										"New Mexico",
										"Arizona",
										"Nevada",
										"Colorado",
										"Oregon",
										"Ohio",
										"New Jersey"
								)
								.sorted()
								.toList());
		form.add(region);

	}

	private void postalCode() {

		var postalCode =
				new TextField<>(
						"postalCode",
						LambdaModel.of(form.getModel(), User::getPostalCode, User::setPostalCode));
		form.add(postalCode);

	}
}
