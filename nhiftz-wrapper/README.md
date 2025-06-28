# NHIF API Client

A lightweight Java client for interacting with the National Health Insurance Fund (NHIF) API. This client provides a simple and type-safe way to access NHIF services including member verification, claims submission, and more.

## Features

- **Member Verification**: Verify member details and eligibility
- **Claims Management**: Submit and track claims
- **Patient History**: Access patient visit history and medical records
- **Asynchronous API**: Non-blocking API using Java's CompletableFuture
- **Thread-Safe**: Safe to use in concurrent applications
- **Comprehensive Error Handling**: Detailed exceptions for error scenarios

## Requirements

- Java 11 or higher
- Maven 3.6 or higher (for building from source)

## Installation

### Maven

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.oau.nhif</groupId>
    <artifactId>nhif-api-client</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

```gradle
implementation 'com.oau.nhif:nhif-api-client:1.0.0'
```

## Usage

### Creating a Client

```java
import com.oau.nhif.client.*;

// Create a client with basic configuration
NhifApiClient client = NhifApiClientFactory.createClient(
    "https://verification.nhif.or.tz",  // NHIF Auth base URL
    "http://test.nhif.or.tz/servicehub", // NHIF Service base URL
    "your-client-id",                   // Your client ID
    "your-client-secret",               // Your client secret
    "your-username"                     // Your username
);

// Or with custom timeouts
NhifApiConfig config = NhifApiConfig.builder()
    .authBaseUrl("https://verification.nhif.or.tz")
    .serviceBaseUrl("http://test.nhif.or.tz/servicehub")
    .clientId("your-client-id")
    .clientSecret("your-client-secret")
    .username("your-username")
    .connectionTimeout(Duration.ofSeconds(5))
    .readTimeout(Duration.ofSeconds(10))
    .build();

NhifApiClient clientWithConfig = new DefaultNhifApiClient(config);

// Don't forget to close the client when done
try (NhifApiClient clientToClose = client) {
    // Use the client
}
```

### Example: Getting Member Details

```java
String cardNumber = "1234567890";

// Using CompletableFuture
try (NhifApiClient client = NhifApiClientFactory.createClient("https://api.nhif.or.tz", 
                                                         "client-id", "secret", "username")) {
    
    client.getCardDetails(cardNumber)
        .thenAccept(card -> {
            System.out.println("Member Name: " + card.getMemberName());
            System.out.println("Card Status: " + card.getCardStatus());
            System.out.println("Expiry Date: " + card.getCardExpiryDate());
        })
        .exceptionally(throwable -> {
            System.err.println("Error: " + throwable.getMessage());
            return null;
        })
        .get(); // Block until complete (in a real app, you'd typically not block)
}
```

### Example: Submitting a Claim

```java
import com.oau.nhif.client.model.*;

FolioSubmission folio = new FolioSubmission();
folio.setFacilityCode("FAC001");
folio.setVisitDate("2025-06-28");
folio.setCardNumber("1234567890");
folio.setMemberNumber("M1234567");
folio.setPatientName("John Doe");
folio.setGender("M");
folio.setDateOfBirth("1980-01-01");
folio.setDiagnosis("A09 - Diarrhoea and gastroenteritis");

FolioSubmission.ClaimItem item = new FolioSubmission.ClaimItem();
item.setItemCode("CONSULT");
item.setDescription("Consultation");
item.setQuantity(1);
item.setUnitPrice(10000);
item.setTotalPrice(10000);

folio.setItems(List.of(item));

try (NhifApiClient client = NhifApiClientFactory.createClient("https://api.nhif.or.tz", 
                                                         "client-id", "secret", "username")) {
    
    client.submitFolio(folio)
        .thenAccept(response -> {
            if (response.isSuccess()) {
                System.out.println("Claim submitted successfully");
                System.out.println("Claim Number: " + response.getClaimNumber());
                System.out.println("Amount Claimed: " + response.getAmountClaimed());
            } else {
                System.err.println("Claim submission failed: " + response.getMessage());
            }
        })
        .exceptionally(throwable -> {
            System.err.println("Error: " + throwable.getMessage());
            return null;
        })
        .get(); // Block until complete
}
```

## Error Handling

The client throws `NhifApiException` for API-related errors. Always handle these exceptions in your code:

```java
try {
    client.getCardDetails("1234567890").get();
} catch (NhifApiException e) {
    System.err.println("API Error: " + e.getMessage());
    System.err.println("Status Code: " + e.getStatusCode());
    System.err.println("Response: " + e.getResponseBody());
} catch (InterruptedException | ExecutionException e) {
    System.err.println("Error: " + e.getMessage());
}
```

## Configuration

You can configure the client using the `NhifApiConfig` class:

```java
NhifApiConfig config = NhifApiConfig.builder()
    .authBaseUrl("https://verification.nhif.or.tz")
    .serviceBaseUrl("http://test.nhif.or.tz/servicehub")
    .clientId("your-client-id")
    .clientSecret("your-client-secret")
    .username("your-username")
    .connectionTimeout(Duration.ofSeconds(5))
    .readTimeout(Duration.ofSeconds(10))
    .maxRetries(3)
    .enableLogging(true)
    .build();

NhifApiClient client = new DefaultNhifApiClient(config);
```

## Building from Source

```bash
git clone https://github.com/yourusername/nhif-api-client.git
cd nhif-api-client
mvn clean install
```

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Support

For support, please open an issue in the GitHub repository.

## Acknowledgments

- NHIF for providing the API
- All contributors who have helped improve this client
