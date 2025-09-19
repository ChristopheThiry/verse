# French Versification Syllable Counter

This project is a Spring Boot backend application designed to count the syllables in French poetry according to the rules of French versification. It provides a simple REST API to submit lines of a poem and receive their corresponding syllable counts.

## Features

*   REST API endpoint for syllable counting.
*   Implements core rules of French versification, including:
    *   Handling of the mute 'e' (`e caduc`).
    *   Elision between words.
    *   Basic heuristics for diérèse.
*   Built with Java 21 and Spring Boot.
*   Managed with Apache Maven.
*   Includes a suite of JUnit 5 tests.
*   Provides an interactive API documentation via Swagger UI.

## Technologies Used

*   **Java 21**
*   **Spring Boot 3.2.2**
*   **Apache Maven**
*   **SpringDoc OpenAPI (Swagger UI)**

## Interactive API Documentation (Swagger UI)

This project includes an interactive OpenAPI (Swagger) UI for exploring and testing the API endpoints. Once the application is running, you can access the Swagger UI at the following URL:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

The UI allows you to see the API structure and send test requests directly from your browser.

## API Usage

To use the service, send a `POST` request to the following endpoint:

`/api/v1/count-syllables`

### Request Body

The request body should be a JSON object containing a list of strings, where each string is a line from the poem.

**Example:**
```json
{
    "lines": [
        "Ma seule étoile est morte, - et mon luth constellé",
        "Porte le Soleil noir de la Mélancolie."
    ]
}
```

### Response Body

The service will respond with a JSON object containing a list of integers, representing the syllable count for each line.

**Example:**
```json
{
    "syllable_counts": [
        12,
        12
    ]
}
```

## How to Run the Application

### Prerequisites

*   Java 17 JDK
*   Apache Maven

### Build

To build the project and create an executable JAR, run the following command from the project root:

```bash
mvn clean install
```

### Run

Once the project is built, you can run the application with the following command:

```bash
java -jar target/french-versification-0.0.1-SNAPSHOT.jar
```

The application will start on the default port `8080`.

## How to Run Tests

To run the unit tests for the application, execute the following Maven command:

```bash
mvn test
```
