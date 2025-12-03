# Smart Planner

A Java Swing desktop application for task management and scheduling with user authentication, built using Maven and Java 17.

## Table of Contents
- [Features](#features)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [How to Run](#how-to-run)
- [Data Storage System](#data-storage-system)
- [Application Workflow](#application-workflow)
- [Components Overview](#components-overview)
- [Security](#security)

## Features

- **User Authentication**: Secure sign-up and login with encrypted credentials
- **Task Management**: Create, edit, delete, and track tasks with:
  - Title, description, due date, and due time
  - Priority levels (High, Medium, Low)
  - Categories for organization
  - Completion status tracking
  - Overdue task detection
- **Weekly Timetable**: Manage two separate weekly schedules with customizable time slots
- **Task Reports**: Generate reports showing pending, late, and completed tasks
- **Profile Management**: Change username and password
- **GUI Interface**: User-friendly Java Swing interface

## Project Structure

```
smart-planner/
├── pom.xml                          # Maven configuration
├── data/                            # CSV-based data storage
│   ├── login_info.csv              # Encrypted user credentials
│   ├── planner.csv                 # Task data
│   ├── time_table.csv              # Primary timetable
│   └── time_table2.csv             # Secondary timetable
└── src/main/java/com/smartplanner/
    ├── Main.java                    # Application entry point
    ├── auth/
    │   └── AuthenticationManager.java    # Handles user authentication
    ├── core/
    │   └── SmartPlannerApplication.java  # Core application launcher
    ├── data/
    │   └── FileManager.java         # Manages data file initialization
    ├── model/
    │   ├── Task.java                # Task data model
    │   ├── TaskReport.java          # Report data model
    │   └── TimeTableEntry.java      # Timetable entry model
    ├── service/
    │   ├── PlannerService.java      # Task business logic
    │   └── TimeTableService.java    # Timetable business logic
    ├── ui/
    │   ├── components/
    │   │   ├── BasePage.java        # Base UI component
    │   │   └── TablePanel.java      # Table display component
    │   └── pages/
    │       ├── LandingPage.java     # Welcome screen
    │       ├── LoginPage.java       # Login screen
    │       ├── SignUpPage.java      # Registration screen
    │       ├── MainPage.java        # Main navigation
    │       ├── PlannerPage.java     # Task management UI
    │       ├── TimeTablePage.java   # Timetable management UI
    │       ├── ReportPage.java      # Task reports UI
    │       ├── ChangeUsernamePage.java
    │       └── ChangePasswordPage.java
    ├── utils/
    │   ├── CryptoUtils.java         # Encryption utilities
    │   └── DataUtils.java           # Data utilities
    └── validation/
        ├── DateValidator.java       # Date validation interface
        └── DateFormatValidator.java # Date format validation
```

## Prerequisites

- **Java Development Kit (JDK) 17** or higher
- **Apache Maven 3.6+** (for building and running)
- Any Java-compatible operating system (Windows, macOS, Linux)

## Installation

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd cprogramming-smart_planner
   ```

2. **Verify Java installation**:
   ```bash
   java -version
   ```
   Should display Java version 17 or higher.

3. **Verify Maven installation**:
   ```bash
   mvn -version
   ```

## How to Run

### Method 1: Using Maven (Recommended)

```bash
mvn clean compile exec:java
```

This command will:
- Clean previous builds
- Compile the source code
- Execute the main class (`com.smartplanner.Main`)

### Method 2: Build JAR and Run

```bash
# Build the project
mvn clean package

# Run the JAR file
java -cp target/smart-planner-1.0-SNAPSHOT.jar com.smartplanner.Main
```

### Method 3: Using IDE

1. Import the project as a Maven project in your IDE (IntelliJ IDEA, Eclipse, VS Code)
2. Let the IDE resolve Maven dependencies
3. Run the `Main.java` class located at `src/main/java/com/smartplanner/Main.java`

## Data Storage System

Smart Planner uses a **CSV-based file storage system** instead of a traditional database. All data is stored in the `data/` directory.

### Data Files

#### 1. `login_info.csv` - User Credentials
- **Format**: `encrypted_username,encrypted_password`
- **Encryption**: Uses Caesar cipher (shift value: 101)
- **Single User**: Only one user account allowed per installation
- **Example** (encrypted): `¤¥¦§¨,©ª«¬­®`

#### 2. `planner.csv` - Tasks
- **Format**: `id,title,description,dueDate,dueTime,priority,completed,category`
- **Example**: 
  ```
  TASK_1234567890_123,Finish report,Complete quarterly report,2024-12-01,14:00,High,false,Work
  TASK_1234567891_456,Buy groceries,Get milk and bread,2024-11-30,18:00,Medium,true,Personal
  ```

#### 3. `time_table.csv` & `time_table2.csv` - Timetables
- **Format**: `time,monday,tuesday,wednesday,thursday,friday,saturday,sunday`
- **Two Tables**: Allows switching between two different weekly schedules
- **Example**:
  ```
  08:00,Math Class,Physics Lab,Math Class,Physics Lab,Math Class,,
  09:00,English,English,Chemistry,English,Chemistry,,
  10:00,Break,Break,Break,Break,Break,,
  ```
- **Special Entry**: Last row contains `00:00,,,,,,null,` as a sentinel value

### Data Flow

1. **Initialization**: `FileManager` creates all required data files on first run
2. **Loading**: Service classes (`PlannerService`, `TimeTableService`, `AuthenticationManager`) read from CSV files
3. **In-Memory**: Data is stored in memory during runtime (Lists, HashMaps)
4. **Persistence**: Changes are immediately written back to CSV files
5. **Encryption**: Credentials are encrypted before storage and decrypted on read

### Why CSV Instead of Database?

- **Simplicity**: No database server required
- **Portability**: Data files can be easily backed up or transferred
- **Transparency**: Human-readable format for debugging
- **Zero Configuration**: No connection strings or database setup
- **Lightweight**: Suitable for single-user desktop application

## Application Workflow

### First Time Launch

1. Application creates the `data/` directory and initializes CSV files
2. **Landing Page** appears with only "Sign Up" enabled
3. User creates account (credentials encrypted and stored)
4. After sign up, only "Log In" button will be available on future launches

### Regular Usage

1. **Login** with username and password
2. **Main Page** provides navigation to:
   - **Planner**: Manage tasks
   - **Time Table**: Manage weekly schedules
   - **Profile Menu**: Change username/password
3. **Planner Features**:
   - Add tasks with details (title, description, due date/time, priority, category)
   - Edit or delete existing tasks
   - Mark tasks as complete
   - View task reports (pending, late, completed)
   - Search and filter tasks
4. **Timetable Features**:
   - Add/edit time slots and activities
   - Switch between two timetables
   - View weekly schedule

## Components Overview

### Core Components

- **Main.java**: Application entry point, delegates to `SmartPlannerApplication`
- **SmartPlannerApplication**: Initializes data files and launches the UI

### Authentication Layer

- **AuthenticationManager**: 
  - Loads encrypted credentials from CSV
  - Validates user login
  - Checks if user exists

### Data Layer

- **FileManager**: Creates and initializes all CSV files
- **Models**: POJOs (Plain Old Java Objects) representing data structures
  - `Task`: Represents a planner task
  - `TimeTableEntry`: Represents a timetable row
  - `TaskReport`: Report aggregation object

### Service Layer

- **PlannerService**: 
  - CRUD operations for tasks
  - Task filtering (by priority, category, completion status)
  - Overdue task detection
  - Report generation
  
- **TimeTableService**:
  - CRUD operations for timetable entries
  - Switch between two timetables
  - Time slot management

### UI Layer

- **Pages**: JFrame-based screens for different functionalities
- **Components**: Reusable UI components like `BasePage` and `TablePanel`

### Utilities

- **CryptoUtils**: Caesar cipher encryption/decryption
- **DataUtils**: Data manipulation helpers
- **Validators**: Input validation (dates, formats)

## Security

### Encryption

- **Algorithm**: Caesar cipher with shift value of 101
- **Usage**: Username and password encryption only
- **Note**: Caesar cipher provides basic obfuscation but is not cryptographically secure. For production use, consider implementing stronger encryption (e.g., BCrypt, AES).

### Single User Design

- Application supports only **one user account** per installation
- Sign up is disabled after first account creation
- To create a new account, delete `data/login_info.csv`

## Development

### Build Commands

```bash
# Compile
mvn compile

# Run tests
mvn test

# Run specific test class
mvn test -Dtest=TaskTest

# Run tests with coverage (requires jacoco plugin)
mvn test jacoco:report

# Package as JAR
mvn package

# Clean build artifacts
mvn clean

# Clean, compile and test
mvn clean test
```

### Test Structure

The project includes comprehensive JUnit 5 tests covering:

- **Model Tests**: `Task`, `TimeTableEntry`, `TaskReport`
- **Service Tests**: `PlannerService`, `TimeTableService`
- **Authentication Tests**: `AuthenticationManager`
- **Data Tests**: `FileManager`
- **Utility Tests**: `CryptoUtils`, `DateFormatValidator`

Test files are located in `src/test/java/com/smartplanner/` with the same package structure as the main source.

#### Test Coverage

- **Models**: Full coverage of getters, setters, CSV serialization/deserialization
- **Services**: CRUD operations, filtering, searching, data persistence
- **Authentication**: Credential validation, encryption/decryption
- **Utilities**: Caesar cipher encryption with various inputs
- **File Management**: File creation and initialization

#### Running Tests

```bash
# Run all tests
mvn test

# Run tests with detailed output
mvn test -Dtest=SmartPlannerTestSuite

# Run a specific test class
mvn test -Dtest=TaskTest

# Run a specific test method
mvn test -Dtest=TaskTest#testDefaultConstructor
```

Test results are generated in `target/surefire-reports/`.

### Adding New Features

1. **Models**: Add to `src/main/java/com/smartplanner/model/`
2. **Services**: Add business logic to `src/main/java/com/smartplanner/service/`
3. **UI**: Add pages to `src/main/java/com/smartplanner/ui/pages/`
4. **Data Storage**: Update `FileManager` and service classes for new CSV files
5. **Tests**: Add corresponding test classes in `src/test/java/com/smartplanner/`

### Testing Best Practices

- Write tests for all new features
- Maintain test coverage above 70%
- Use `@TempDir` for file system operations in tests
- Mock external dependencies when appropriate
- Follow the Arrange-Act-Assert pattern

## Troubleshooting

### Application Won't Start
- Verify Java 17+ is installed: `java -version`
- Check Maven is properly configured: `mvn -version`
- Ensure `data/` directory has write permissions

### Login Issues
- Check `data/login_info.csv` exists and is not corrupted
- Delete file to reset and create new account

### Data Not Persisting
- Verify write permissions in `data/` directory
- Check CSV files are not locked by another application

## License

This project is part of a C programming course assignment.

## Contributors

Course: C Programming - Smart Planner Project