# AGENTS.md

This file contains guidelines and commands for agentic coding agents working in this Spring Boot WebFlux repository.

## Project Overview

This is a Spring Boot 3.5.7 WebFlux application using Java 25 with reactive programming patterns. The project demonstrates HTTPS client integration with custom SSL certificate trust and follows standard Maven project structure.

## Build and Development Commands

### Maven Commands
- **Build project**: `./mvnw clean compile` or `mvn clean compile`
- **Run application**: `./mvnw spring-boot:run` or `mvn spring-boot:run`
- **Package JAR**: `./mvnw clean package` or `mvn clean package`
- **Run tests**: `./mvnw test` or `mvn test`
- **Run single test**: `./mvnw test -Dtest=ClassName#methodName` or `mvn test -Dtest=ClassName#methodName`
- **Run test class**: `./mvnw test -Dtest=ClassName` or `mvn test -Dtest=ClassName`

### Testing Commands
- **Run all tests**: `./mvnw test`
- **Run specific test**: `./mvnw test -Dtest=SomeHttpsServiceTest#testPing`
- **Run test class**: `./mvnw test -Dtest=SomeHttpsServiceTest`
- **Run with coverage**: `./mvnw test jacoco:report` (if jacoco plugin is added)

## Code Style Guidelines

### Package Structure
```
io.will.webfluxdemo/
├── WebfluxDemo002Application.java (Main class)
├── config/ (Configuration classes)
├── controller/ (REST controllers)
├── client/ (HTTP client interfaces)
└── service/ (Business logic - add as needed)
```

### Naming Conventions
- **Classes**: PascalCase (e.g., `WebClientConfig`, `SomeHttpsServiceController`)
- **Methods**: camelCase (e.g., `createTrustSpecificSslContext`, `ping`)
- **Variables**: camelCase with descriptive names
- **Constants**: UPPER_SNAKE_CASE
- **Packages**: lowercase with domain reverse pattern

### Import Organization
1. Java standard library imports
2. Third-party library imports (Spring, Reactor, etc.)
3. Application-specific imports
4. Static imports (if any)

Use fully qualified imports, avoid wildcard imports (`import.*`).

### Code Formatting
- **Indentation**: 4 spaces (no tabs)
- **Line length**: Maximum 120 characters
- **Braces**: K&R style - opening brace on same line
- **Spacing**: Single blank line between methods, logical groups

### Spring Boot Specific Patterns

#### Configuration Classes
- Use `@Configuration` annotation
- Separate concerns into different configuration classes
- Use `@Bean` methods for component creation
- Constructor injection preferred over field injection

#### Controllers
- Use `@RestController` for REST endpoints
- Return reactive types (`Mono<T>`, `Flux<T>`)
- Constructor-based dependency injection
- Use appropriate HTTP method annotations (`@GetMapping`, `@PostMapping`, etc.)

#### HTTP Clients
- Use declarative HTTP client interfaces with `@GetExchange`, `@PostExchange`
- Configure WebClient in separate `@Configuration` classes
- Handle SSL/TLS configuration properly for external services

### Reactive Programming Guidelines

#### Return Types
- **Single value**: `Mono<T>`
- **Multiple values**: `Flux<T>`
- **No return value**: `Mono<Void>`

#### Error Handling
- Use reactive operators for error handling (`.onError()`, `.onErrorResume()`)
- Throw runtime exceptions with descriptive messages
- Proper exception chaining with original cause

#### Resource Management
- Use try-with-resources for InputStream and other closeable resources
- Handle SSL context creation with proper exception handling
- Clean up resources in reactive streams when needed

### Testing Guidelines

#### Test Structure
- Use `@WebFluxTest` for web layer testing
- Mock dependencies with `@MockBean`
- Use `WebTestClient` for reactive endpoint testing
- Test both success and error scenarios

#### Test Naming
- Use descriptive test method names: `testMethodName_Scenario_ExpectedResult()`
- Follow pattern: `test[Feature]_[Condition]_[ExpectedOutcome]()`

#### Test Assertions
- Use WebTestClient's fluent API for assertions
- Mock reactive return types properly (`Mono.just()`, `Flux.just()`)
- Test status codes, headers, and response bodies

### Security Guidelines

#### SSL/TLS Configuration
- Store certificates in `src/main/resources/`
- Use PEM format for certificates
- Create custom SSL contexts for specific certificate trust
- Handle SSL exceptions gracefully

#### Certificate Management
- Load certificates from classpath resources
- Use proper KeyStore initialization
- Implement certificate trust managers for external services

### Error Handling Patterns

#### Exception Types
- Use `RuntimeException` for unrecoverable errors
- Include descriptive error messages
- Chain exceptions with proper cause handling

#### Logging
- Add appropriate logging for debugging SSL issues
- Log errors with context and stack traces
- Avoid logging sensitive information (certificates, keys)

### Dependencies and Libraries

#### Core Dependencies
- `spring-boot-starter-webflux` - Reactive web framework
- `spring-boot-starter-test` - Testing framework
- `reactor-test` - Reactive testing utilities

#### When Adding Dependencies
- Check if already available in Spring Boot starter parent
- Use compatible versions with Spring Boot 3.5.7
- Update this file when adding new dependencies

### Git Workflow
- Use conventional commit messages
- Create feature branches for new functionality
- Ensure tests pass before committing
- Use `./mvnw test` to verify changes

### IDE Configuration
- Java 25 SDK required
- Maven integration for dependency management
- Spring Boot plugin support for running applications

## Common Patterns

### WebClient Configuration
```java
@Configuration
public class WebClientConfig {
    @Bean
    public SomeClient someClient() {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://api.example.com")
                .clientConnector(customConnector())
                .build();
        // Create and return client proxy
    }
}
```

### Controller Pattern
```java
@RestController
public class SomeController {
    private final SomeClient someClient;
    
    public SomeController(SomeClient someClient) {
        this.someClient = someClient;
    }
    
    @GetMapping("/api/endpoint")
    public Mono<ResponseType> getMethod() {
        return someClient.callService();
    }
}
```

### Test Pattern
```java
@WebFluxTest(controllers = SomeController.class)
class SomeControllerTest {
    @Autowired
    private WebTestClient webTestClient;
    
    @MockBean
    private SomeClient someClient;
    
    @Test
    void testEndpoint() {
        when(someClient.callService()).thenReturn(Mono.just(response));
        
        webTestClient.get()
                .uri("/api/endpoint")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResponseType.class);
    }
}
```

## Notes for Agents

- Always run tests after making changes: `./mvnw test`
- Follow reactive programming patterns throughout the codebase
- Handle SSL/TLS configuration carefully for external service integration
- Use constructor injection for all dependencies
- Maintain the existing package structure and naming conventions
- Update this AGENTS.md file when adding new patterns or conventions