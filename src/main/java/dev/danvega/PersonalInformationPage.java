package dev.danvega;

import java.util.stream.Stream;

import org.apache.wicket.Component;
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
	private Model<User> userModel;

	@Override
	protected void onInitialize() {

		super.onInitialize();

		userModel = Model.of(new User());
		form(
				firstName(),
				lastName(),
				email(),
				country(),
				streetAddress(),
				city(),
				region(),
				postalCode()
		);

	}

	private void form(Component... elements) {

		form = new Form<>("form", userModel) {

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

		Stream.of(elements).forEach(e -> form.add(e));

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

	private Component firstName() {

		var firstName =
				new TextField<>(
						"firstName",
						new PropertyModel<>(userModel, "firstName"));
		firstName.add(new PropertyValidator<>());
		return firstName;

	}

	private Component lastName() {

		var lastName =
				new TextField<>(
						"lastName",
						LambdaModel.of(userModel, User::getLastName, User::setLastName));
		lastName.setRequired(true);
		return lastName;

	}

	private Component email() {

		return
				new EmailTextField(
						"email",
						LambdaModel.of(userModel, User::getEmail, User::setEmail));

	}

	private Component country() {

		return
				new DropDownChoice<>(
						"country",
						LambdaModel.of(userModel, User::getCountry, User::setCountry),
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

	}

	private Component streetAddress() {

		return
				new TextField<>(
						"streetAddress",
						LambdaModel.of(userModel, User::getStreetAddress, User::setStreetAddress));

	}

	private Component city() {

		return
				new TextField<>(
						"city",
						LambdaModel.of(userModel, User::getCity, User::setCity));

	}

	private Component region() {

		return
				new DropDownChoice<>(
						"region",
						LambdaModel.of(userModel, User::getRegion, User::setRegion),
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

	}

	private Component postalCode() {

		return
				new TextField<>(
						"postalCode",
						LambdaModel.of(userModel, User::getPostalCode, User::setPostalCode));

	}
}
