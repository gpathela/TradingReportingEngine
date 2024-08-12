# Trading Reporting Engine

## Overview
The Trading Reporting Engine is a Java-based application built with Spring Boot that reads a set of XML event files, extracts specific elements, stores them in a database, filters the events based on a set of criteria, and reports the events in JSON format.

## Features
- **XML Parsing**: Uses `javax.xml.parsers.DocumentBuilder` and `javax.xml.xpath.XPath` to parse XML files.
- **Database Storage**: Stores extracted data into a database using JPA.
- **Filtering**: Filters events based on specified criteria.
- **JSON Reporting**: Reports filtered events in JSON format via an HTTP response.
- **Extensible Design**: Designed to easily add more filtering criteria without impacting existing filters.

## Requirements
- Java 11 or higher
- Maven
- Spring Boot
- A relational database (e.g., MySQL, PostgreSQL)

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

## Usage
1. **Place XML files**: Place your XML event files in the `src/main/resources/events` directory.
2. **Access the API**: Use the following endpoint to get the filtered events in JSON format:
    ```http
    GET /api/trades
    ```

## Example XML File
```xml
<event>
    <buyerPartyReference href="BUYER_BANK"/>
    <sellerPartyReference href="EMU_BANK"/>
    <paymentAmount>
        <amount>100.0</amount>
        <currency>AUD</currency>
    </paymentAmount>
</event>
