# Apache Wicket + Forms

Welcome to the Apache Wicket + Forms project!
This Spring Boot application showcases the integration of Apache Wicket with form handling, providing a robust
foundation for building web applications with server-side rendering.

It's based on [this project](https://github.com/danvega/jte-forms) by [Dan Vega](https://www.danvega.dev/) about Spring Boot and JTE integration, while trying to introduce Apache Wicket
as another great open-source alternative for building web applications with server-side rendering and Java.
For more info about Apache Wicket please visit: [Apache Wicket homepage](https://wicket.apache.org/).

## Project Overview

This project demonstrates how to:

- Set up a Spring Boot application with Apache Wicket
- Handle form submissions
- Perform data validation
- Interact with a PostgreSQL database using Spring Data JDBC

## Project Requirements

- Java 23
- Maven
- Docker (for running PostgreSQL)

## Dependencies

This project relies on the following main dependencies:

- Spring Boot 3.3.4
- Spring Web
- Spring Data JDBC
-
- PostgreSQL
- Spring Boot Docker Compose
- Spring Boot Validation

## Getting Started

To get started with this project, follow these steps:

1. Ensure you have Java 23 and Maven installed on your system.
2. Make sure Docker is running on your machine (for PostgreSQL).

## How to Run the Application

1. Clone the repository (if you haven't already).
2. Navigate to the project root directory.
3. Run the following command to start the application:

```bash
./mvnw spring-boot:run
```

This command will:

- Download all necessary dependencies
- Compile the project
- Start a PostgreSQL container using Docker Compose
- Run the Spring Boot application

Once started, you can access the application by opening a web browser and navigating to `http://localhost:8080`.

![Form](/images/personal_info_form.png)

## Relevant Code Examples

Let's look at some key parts of the application:

### User Model

The `User` class represents the data model for user information:

```java

@Table("users")
public class User {
	@Id
	private Long id;
	@NotBlank(message = "The First Name field should not be blank.")
	private String firstName;
	@NotBlank(message = "The Last Name field should not be blank.")
	private String lastName;
	private String email;
	// ... other fields and methods
}
```

This class uses annotations for database mapping (`@Table`) and validation (`@NotBlank`).

### PersonalInformationPage

The `PersonalInformationPage` is Wicket's WebPage implementation handling all the functionality
required by the application in the component-based manner. It uses plain HTML as its template view
with all the 'dynamics' handled by Wicket components themselves, thus the functionality can be tested
relatively easy with plain JUnit tests supported by WicketTester :

```java
public class PersonalInformationPage extends WebPage {
	//... 
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
		//... for the rest of the implementation see the source file itself
	}
}
```

## Conclusion

The Apache Wicket + Forms project provides a solid starting point for building web applications with Spring Boot and
Apache Wicket.
It showcases form handling, data validation, and database interactions, all while leveraging the power of server-side
rendering.

Feel free to explore the code, experiment with the form submission, and extend the application to suit your needs.
If you have any questions or run into issues, please don't hesitate to open an issue in the repository.

Happy coding!




