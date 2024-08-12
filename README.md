# Trading Reporting Engine

## Overview
The Trading Reporting Engine is a Java-based application built with Spring Boot that reads a set of XML event files, extracts specific elements, stores them in a database, filters the events based on a set of criteria, and reports the events in JSON format.

## Design and Implementation
The application follows a layered architecture with the following key components:
- **Controller**: Handles HTTP requests and responses.
- **Service**: Contains business logic for processing trades & parsing data from XML file.
- **Repository**: Interacts with the database using JPA.
- **Model**: Represents the data structure of trades and events.

Design patterns used:
- **Singleton**: Ensures a single instance of the database connection.
- **Factory**: Creates instances of trade objects based on XML input.
- **Strategy**: Implements different filtering criteria.


## Features
- **XML Parsing**: Uses `javax.xml.parsers.DocumentBuilder` and `javax.xml.xpath.XPath` to parse XML files.
- **Database Storage**: Stores extracted data into a H2 database using JPA.
- **Filtering**: Filters events based on specified criteria.
- **JSON Reporting**: Reports filtered events in JSON format via an HTTP response.
- **Extensible Design**: Designed to easily add more filtering criteria without impacting existing filters.
- **API Documentation**: Uses Swagger for API documentation and testing. htttp://localhost:8080/swagger.html

## API Details
- **Find all trades: Provides a pageable list of all trades
- **Find by Default condition: A default query is configured for quick access
- **Find by Conditions: Combination of Buyer, Seller, Currency & Amount (EQUALS, GREATER_THAN, LESS_THAN). More that one condition can be used to search. Consider following condition to search
```
{
    "conditions": [
        {
            "buyer_party": "KANMU_EB",
            "premium_amount": 300,
            "premium_amount_condition": "EQUALS",
            "seller_party": "EMU_BANK",
            "premium_currency": "AUD"
        },
        {
        "buyer_party": "EMU_BANK",
        "seller_party": "BISON_BANK",
        "premium_currency": "USD",
        "premium_amount": 400,
        "premium_amount_condition": "GREATER_THAN"
        }
    ]
}
```

## Requirements
- Java 11 or higher
- Maven
- Spring Boot


## Setup and Installation
1. **Clone the repository**:
    ```sh
    git clone https://github.com/yourusername/trading-reporting-engine.git
    cd trading-reporting-engine
    ```

2. **Build the project**:
    ```sh
    mvn clean install
    ```

3. **Run the application**:
    ```sh
    mvn spring-boot:run
    ```


## Running Tests
To run the tests for the project, use the following Maven command:
```sh
mvn test
```

## Usage
1. **Place XML files**: Place your XML event files in the `src/main/resources/events` directory.
2. **Access the API**: Use the following endpoint to get the filtered events in JSON format:
    ```http
    GET /api/trades
    ```

## Assumptions and Trade-offs
- The XML files are well-formed and follow a consistent structure.
- Trade-offs were made to prioritize simplicity and readability over performance in some areas.
