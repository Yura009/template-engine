
# Messenger Template Engine

## Overview

This project is a **custom email messenger** application that mimics sending emails using a **template generation engine** developed from scratch with a strong **Test-Driven Development (TDD)** approach. The application supports **console mode** and **file mode** for input and output.

The template engine is built **without any third-party libraries** and is fully tested using **JUnit 5**, with various testing techniques including parameterized tests, mocks, spies, custom JUnit extensions, and more.

## Project Structure

```
template-engine/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com.example.messenger/
│   │           ├── Messenger.java              # Main application logic
│   │           ├── MessengerApp.java           # Entry point (main method)
│   │           ├── engine/
│   │           │   └── TemplateEngine.java     # Core template parsing & rendering
│   └── test/
│       └── java/
│           └── com.example.messenger/
│               ├── MessengerConsoleModeTest.java   # Console mode tests (mocked Scanner, output)
│               ├── MessengerFileModeTest.java      # File mode tests with temp folders & mocks
│               ├── engine/
│               │   └── TemplateEngineTest.java     # TDD-based tests for template engine
│               └── extension/
│                   └── LoggingExtensionTest.java   # Custom JUnit extension logging to file
├── build.gradle / pom.xml                       # Build configuration (Gradle or Maven)
├── README.md                                    # Project description
```

## Features

- 🧩 **Custom Template Engine**  
  Supports placeholder substitution in the format `#{key}`.

- ❌ **Strict Mode Validation**  
  Missing placeholder values throw descriptive exceptions.

- 🧃 **Ignores Extra Variables**  
  Runtime values not present in template are ignored.

- 🔁 **Nested Placeholder Support**  
  Allows values like `#{value} = #{tag}` → result will include `#{tag}`.

- 🌐 **Latin-1 Character Support**  
  Templates and variables fully support Latin-1 charset.

- 🖥 **Console Mode**  
  Reads template and variables from console input.

- 📁 **File Mode**  
  Processes templates from file, writes results to output file.

## How It Works

### Console Mode

Run the application without arguments to enter interactive mode:

```shell
java -jar messenger.jar
```

Input:
```
Hello, #{name}!
name=Yurii
end
```

Output:
```
Result: Hello, Yurii!
```

### File Mode

Run the application with input and output file paths:

```shell
java -jar messenger.jar input.txt output.txt
```

Example `input.txt`:
```
Hello, #{name}!
name=Yurii
```

Output will be written to `output.txt`.

---

## Testing and Quality Assurance

The project was developed using **TDD** and includes advanced testing techniques. All tests are written using **JUnit 5**.

### ✔ Covered Testing Techniques

| Feature | Description |
|--------|-------------|
| ✅ TDD approach | All template engine features developed through test-first workflow |
| ✅ Parameterized Tests | Used for placeholder substitution and edge cases |
| ✅ Dynamic Tests | For generating test cases programmatically |
| ✅ Meta Annotations & Filtering | Custom test tags and annotation processing |
| ✅ TemporaryFolder | Ensures isolated file-based tests |
| ✅ File/Console Mocks | Mocked input/output using `Scanner` and `PrintStream` |
| ✅ Partial Mocks | Used with `Mockito` for selected method interception |
| ✅ Spies | To verify interactions without full mocks |
| ✅ Custom Extension | Logs test execution info to a file (`LoggingExtension`) |
| ✅ ExpectedException | Exception assertions via `assertThrows` and conditions |
| ✅ Conditional Disabling | Disable tests under certain runtime conditions |

### 🧪 Sample Test Classes

- `TemplateEngineTest` – covers all placeholder behaviors using parameterized and dynamic tests.
- `MessengerConsoleModeTest` – verifies full flow in console mode with mocked input/output.
- `MessengerFileModeTest` – ensures file processing using `TemporaryFolder`.
- `LoggingExtensionTest` – validates output of custom JUnit 5 extension.

---

## Build & Run

You can build the project using **Gradle** or **Maven**:

### Gradle
```bash
./gradlew build
./gradlew test
```

### Maven
```bash
mvn clean install
mvn test
```

---

## Requirements

- Java 11+
- Gradle or Maven
- Git
- JUnit 5

---

## Development Guidelines

- Follow clean coding practices
- Ensure tests are descriptive and isolated
- Use meaningful commit messages (especially for TDD steps)
- Ensure full test coverage before implementation
- Use Checkstyle or equivalent to maintain code quality

---

## Author

Developed by [Yurii](https://github.com/Yura009) as part of the **Testing Module Practical Task** assignment.

---
