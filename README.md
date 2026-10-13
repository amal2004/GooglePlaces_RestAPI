# Google Places REST API Automation Framework

A BDD-style REST API automation framework built with **Java, REST Assured, Cucumber, and TestNG**. It demonstrates how to test the Place API by creating, retrieving, and deleting places, while keeping request construction, endpoint definitions, configuration, and assertions in separate components.

> **API under test:** the Rahul Shetty Academy Google Maps-style demo API. This is a public demo service; its availability and behavior may change.

## Table of Contents

- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Framework Design](#framework-design)
- [Scenarios Covered](#scenarios-covered)
- [Prerequisites](#prerequisites)
- [Setup](#setup)
- [Configuration](#configuration)
- [Run Tests](#run-tests)
- [Reports and Logs](#reports-and-logs)
- [Adding a New Scenario](#adding-a-new-scenario)
- [Engineering Notes](#engineering-notes)
- [Troubleshooting](#troubleshooting)

## Technology Stack

| Technology | Purpose |
|---|---|
| Java 11+ | Language for test code |
| Maven | Dependency management and test execution |
| REST Assured | HTTP requests and API response handling |
| Cucumber | Gherkin feature files and BDD step definitions |
| TestNG | Test execution and assertions |
| Jackson | Serialization of Java request objects into JSON |

The TestNG integration uses `io.cucumber:cucumber-testng`. JUnit is not required for the TestNG runner.

## Project Structure

```text
REST_API_TestNG_Framework/
├── pom.xml
├── README.md
└── src/
    └── test/
        ├── java/
        │   ├── client/
        │   │   └── PlaceApiClient.java
        │   ├── config/
        │   │   ├── ConfigReader.java
        │   │   └── RequestSpecFactory.java
        │   ├── data/
        │   │   └── TestDataBuilder.java
        │   ├── pojo/
        │   │   ├── AddPlace.java
        │   │   └── Location.java
        │   ├── resources/
        │   │   └── APIResources.java
        │   ├── runner/
        │   │   └── TestRunner.java
        │   └── steps/
        │       └── PlaceApiSteps.java
        └── resources/
            ├── features/
            │   └── place_api.feature
            └── global.properties
```

## Framework Design

The framework follows a small separation-of-concerns pattern:

- **`PlaceApiClient`** — contains HTTP operations for adding, retrieving, and deleting places.
- **`RequestSpecFactory`** — centralizes the base URL, API key query parameter, JSON headers, and request/response logging.
- **`ConfigReader`** — loads properties from the classpath and allows JVM system properties to override defaults.
- **`APIResources`** — stores endpoint paths in one place.
- **`TestDataBuilder`** — builds request payloads.
- **POJO classes** — model the JSON request body as Java objects.
- **`PlaceApiSteps`** — maps readable Gherkin steps to API operations and uses TestNG assertions.
- **`TestRunner`** — runs Cucumber features through TestNG and creates HTML and JSON reports.

This design keeps Gherkin scenarios focused on behavior rather than implementation details.

## Scenarios Covered

The feature file contains eight scenarios:

1. Add a place successfully.
2. Retrieve a newly created place and verify its name and address.
3. Delete a newly created place successfully.
4. Add a place using Spanish language and different address data.
5. Retrieve a place and verify its name.
6. Retrieve a place and verify its address.
7. Add a place with a longer name and confirm creation.
8. Create and then delete a place.

The scenarios use tags such as `@smoke`, `@regression`, `@data-variation`, and `@lifecycle` to help categorize test coverage.

## Prerequisites

Install the following:

- JDK 11 or later
- Apache Maven 3.8+
- IntelliJ IDEA, Eclipse, or another Java IDE (optional)
- Internet access to reach Maven Central and the demo API

Verify your environment:

```bash
java -version
mvn -version
```

## Setup

1. Clone the repository:

   ```bash
   git clone <YOUR_GITHUB_REPOSITORY_URL>
   ```

2. Open the repository folder:

   ```bash
   cd <YOUR_REPOSITORY_FOLDER>
   ```

3. Confirm `pom.xml` is in the project root.
4. Import the project as a Maven project in your IDE, if desired.
5. Run the test suite:

   ```bash
   mvn clean test
   ```

## Configuration

Default settings are stored in:

```text
src/test/resources/global.properties
```

Example:

```properties
baseUrl=https://rahulshettyacademy.com
apiKey=qaclick123
```

The framework supports overriding these values through Maven system properties:

```bash
mvn clean test -DbaseUrl=https://rahulshettyacademy.com -DapiKey=qaclick123
```

Avoid committing real credentials, private tokens, or production secrets to source control. For a real service, load sensitive values from environment variables or your CI/CD secret store.

## Run Tests

### Run the entire suite

```bash
mvn clean test
```

### Run a tagged subset

The current runner does not set a default Cucumber tag expression. To run only smoke scenarios, add a tag expression to the runner's `@CucumberOptions`, for example:

```java
@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"steps"},
    tags = "@smoke",
    plugin = {
        "pretty",
        "html:target/cucumber-reports/cucumber.html",
        "json:target/cucumber-reports/cucumber.json"
    },
    monochrome = true
)
```

Change `tags` to `@regression`, `@data-variation`, or another expression as needed. Alternatively, configure the tag expression through your build setup.

## Reports and Logs

After execution, inspect:

- **Cucumber HTML report:** `target/cucumber-reports/cucumber.html`
- **Cucumber JSON report:** `target/cucumber-reports/cucumber.json`
- **Maven Surefire/TestNG output:** `target/surefire-reports/`

Request and response logging is enabled by the shared request specification. Review logs carefully and avoid sending secrets or personal data in test payloads.

## Adding a New Scenario

1. Add a scenario to `src/test/resources/features/place_api.feature`.
2. Reuse an existing step definition if the behavior matches.
3. If a new step is needed, implement it in `PlaceApiSteps.java`.
4. Add the corresponding HTTP operation to `PlaceApiClient.java` if the API operation does not exist yet.
5. Keep endpoint paths in `APIResources.java`.
6. Keep payload construction in `TestDataBuilder.java`.
7. Run `mvn clean test` and review the reports.

Prefer asserting observable API behavior—HTTP status, response fields, and returned data—rather than implementation details.

## Engineering Notes

- **Live API dependency:** tests call an external demo API. Network issues, rate limits, service changes, or retained test data can affect results.
- **Cleanup:** create operations may leave records in the demo service unless the test explicitly deletes them. Add cleanup where appropriate.
- **Negative tests:** define expected status codes and error payloads from the API contract before asserting them; do not guess error behavior.
- **Parallel execution:** before enabling parallel scenarios, ensure scenario state is isolated and the API supports concurrent operations safely.
- **Build validation:** after cloning, run `mvn clean test` to confirm dependency resolution and compatibility in your own environment.

## Troubleshooting

### Maven command is not recognized

Install Maven and ensure its `bin` directory is available on your system `PATH`. Restart the terminal and verify with `mvn -version`.

### Java version errors

Confirm `java -version` reports JDK 11 or newer and that `JAVA_HOME` points to the intended JDK.

### Tests fail before making a request

Check Maven dependency resolution, the feature path, the `glue` package, and that the runner is configured for Cucumber TestNG.

### API response differs from expectations

Inspect the request/response logs and verify the live API contract, base URL, API key, and service availability.

---

## License

Add a license file before publishing if you intend to permit reuse. If no license is provided, default copyright rules apply.
