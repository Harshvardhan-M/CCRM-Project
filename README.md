# Campus Course & Records Manager (CCRM)

## Project Overview

The Campus Course & Records Manager (CCRM) is a comprehensive console-based Java application designed for educational institutions to manage students, courses, enrollments, and academic records. This project demonstrates advanced Java SE concepts including OOP principles, design patterns, modern I/O operations, and functional programming features.

## How to Run

### Prerequisites
- JDK 17 or higher
- Any Java IDE (Eclipse recommended)

### Running the Application
```bash
# Compile the project
javac -d bin -sourcepath src src/edu/ccrm/cli/CCRMApplication.java

# Run the application
java -cp bin edu.ccrm.cli.CCRMApplication

# To enable assertions (recommended)
java -ea -cp bin edu.ccrm.cli.CCRMApplication
```

### IDE Setup (Eclipse)
1. File → New → Java Project → "CCRM"
2. Copy source files to src folder
3. Right-click project → Run As → Java Application
4. Select CCRMApplication as main class

## Evolution of Java

### Major Milestones
• **1995** - Java 1.0 released by Sun Microsystems
• **1997** - Java 1.1 introduced inner classes, reflection, RMI
• **1998** - Java 1.2 (J2SE) brought Collections Framework, Swing
• **2000** - Java 1.3 added HotSpot JVM, JNDI
• **2002** - Java 1.4 introduced assertions, regular expressions, NIO
• **2004** - Java 5 added generics, annotations, enums, autoboxing
• **2006** - Java 6 improved performance, scripting support
• **2011** - Java 7 brought try-with-resources, diamond operator, NIO.2
• **2014** - Java 8 introduced lambdas, streams, default methods
• **2017** - Java 9 added modules, JShell, private interface methods
• **2018** - Java 10 introduced var keyword, local variable type inference
• **2018** - Java 11 (LTS) brought HTTP Client, string methods
• **2019** - Java 12-13 introduced switch expressions, text blocks
• **2020** - Java 14-15 added records, sealed classes preview
• **2021** - Java 16-17 (LTS) finalized records, sealed classes, pattern matching
• **2022-2024** - Java 18-21 (LTS) continued with virtual threads, pattern matching enhancements

## Java Platform Editions Comparison

| Feature | Java ME (Micro Edition) | Java SE (Standard Edition) | Java EE (Enterprise Edition) |
|---------|-------------------------|----------------------------|-------------------------------|
| **Target** | Mobile devices, IoT, embedded systems | Desktop applications, standalone apps | Enterprise web applications, servers |
| **Size** | Minimal footprint (~2MB) | Full-featured (~200MB) | Extended features (~500MB+) |
| **APIs** | Basic I/O, networking, UI (MIDP) | Complete standard library | Web services, servlets, EJB, JPA |
| **Use Cases** | Smart cards, sensors, mobile apps | Desktop GUI, utilities, games | Web apps, microservices, distributed systems |
| **Runtime** | KVM (Kilobyte Virtual Machine) | Standard JVM | Application servers (Tomcat, WebLogic) |
| **Examples** | Nokia phones, smart meters | IntelliJ IDEA, NetBeans | Banking systems, e-commerce platforms |

## Java Architecture: JDK, JRE, JVM

### Java Virtual Machine (JVM)
- **Runtime environment** that executes Java bytecode
- **Platform-specific** implementation of Java specification
- **Memory management** through garbage collection
- **Just-In-Time (JIT)** compilation for performance optimization

### Java Runtime Environment (JRE)
- **Contains JVM** plus core libraries and supporting files
- **Minimum requirement** to run Java applications
- **Includes** standard APIs, internationalization support, security features
- **End-user focused** - what users need to run Java programs

### Java Development Kit (JDK)
- **Complete development environment** containing JRE plus development tools
- **Includes compiler (javac)**, debugger, documentation tools
- **Developer-focused** - what programmers need to write Java applications
- **Contains** JRE, so developers don't need separate JRE installation

### Relationship
```
JDK = JRE + Development Tools (javac, javadoc, debugger, etc.)
JRE = JVM + Core Libraries + Supporting Files
JVM = Runtime Engine that executes bytecode
```

## Windows Java Installation Steps

### Step 1: Download JDK
1. Visit Oracle's official website: https://www.oracle.com/java/technologies/downloads/
2. Select "Java 17" (LTS version recommended)
3. Choose "Windows x64 Installer"
4. Download the .exe file

### Step 2: Install JDK
1. Run the downloaded installer as administrator
2. Follow installation wizard (default settings recommended)
3. Installation typically completes to: `C:\Program Files\Java\jdk-17.0.x\`

### Step 3: Set Environment Variables
1. Right-click "This PC" → Properties → Advanced System Settings
2. Click "Environment Variables"
3. Under System Variables, click "New":
   - Variable Name: `JAVA_HOME`
   - Variable Value: `C:\Program Files\Java\jdk-17.0.x`
4. Find "Path" variable, click "Edit", add: `%JAVA_HOME%\bin`

### Step 4: Verify Installation
Open Command Prompt and run:
```bash
java -version
javac -version
```

**Expected Output:**
```
java version "17.0.x" 2023-xx-xx LTS
Java(TM) SE Runtime Environment (build 17.0.x+xx-LTS-xxx)
Java HotSpot(TM) 64-Bit Server VM (build 17.0.x+xx-LTS-xxx, mixed mode)
```

## Eclipse IDE Setup Steps

### Step 1: Download Eclipse
1. Visit: https://www.eclipse.org/downloads/
2. Download "Eclipse IDE for Java Developers"
3. Run installer and select workspace directory

### Step 2: Create New Java Project
1. File → New → Java Project
2. Project Name: "CCRM"
3. Use default location or specify custom path
4. Select "Use default JRE" or configure JDK 17+
5. Click "Finish"

### Step 3: Configure Project
1. Right-click project → Properties
2. Java Build Path → Libraries → Add External JARs (if needed)
3. Project Facets → Enable "Java" version 17+

### Step 4: Run Configuration
1. Right-click main class → Run As → Java Application
2. Or: Run → Run Configurations → New Java Application
3. Main class: `edu.ccrm.cli.CCRMApplication`
4. VM arguments: `-ea` (to enable assertions)

## Implementation Approach

### Architecture Design
The application follows a layered architecture pattern:

1. **CLI Layer** (`edu.ccrm.cli`) - User interface and menu handling
2. **Service Layer** (`edu.ccrm.service`) - Business logic and operations
3. **Domain Layer** (`edu.ccrm.domain`) - Core entities and value objects
4. **I/O Layer** (`edu.ccrm.io`) - File operations and data persistence
5. **Utility Layer** (`edu.ccrm.util`) - Helper classes and common functionality
6. **Configuration Layer** (`edu.ccrm.config`) - Application configuration

### Key Design Decisions

#### OOP Implementation
- **Encapsulation**: All domain classes use private fields with controlled access through getters/setters
- **Inheritance**: Abstract `Person` class extended by `Student` and `Instructor` classes
- **Abstraction**: Abstract methods in `Person` class force concrete implementations
- **Polymorphism**: Method overriding for `toString()`, interface implementations for services

#### Design Patterns Used
- **Singleton Pattern**: `AppConfig` class ensures single configuration instance
- **Builder Pattern**: `Student.Builder` and `Course.Builder` for complex object creation
- **Strategy Pattern**: Service interfaces allow different implementations

#### Modern Java Features
- **Streams API**: Used extensively for data filtering, mapping, and collection operations
- **Lambda Expressions**: Functional programming for comparators and predicates
- **Optional**: Null-safe operations throughout the codebase
- **NIO.2**: Modern file I/O operations with `Path` and `Files` APIs
- **Date/Time API**: `LocalDateTime` for all timestamp operations

#### Exception Handling Strategy
- **Custom Exception Hierarchy**: Base `CCRMException` with specific subclasses
- **Checked vs Unchecked**: Business logic exceptions are checked, validation exceptions are unchecked
- **Try-with-resources**: Automatic resource management for file operations

## Technical Implementation Mapping

| Requirement | Implementation Location |
|-------------|------------------------|
| **OOP Principles** | |
| Encapsulation | All domain classes with private fields, getters/setters |
| Inheritance | Person → Student/Instructor (domain/Person.java) |
| Abstraction | Abstract Person class, Persistable interface |
| Polymorphism | Person references, toString() overrides |
| **Design Patterns** | |
| Singleton | config/AppConfig.java |
| Builder | domain/Course.java (inner Builder class) |
| **Modern Java Features** | |
| Lambdas | util/Comparators.java, Stream operations |
| Streams API | service implementations for filtering/reporting |
| NIO.2 | io/ImportExportService.java, io/BackupService.java |
| Date/Time API | domain classes with LocalDateTime fields |
| **Exception Handling** | |
| Custom Exceptions | domain/exceptions/ package |
| Try-with-resources | File I/O operations |
| **Advanced Concepts** | |
| Nested Classes | Course.Builder (static), Transcript.GradeEntry (inner) |
| Enums | domain/Semester.java, domain/Grade.java |
| Functional Interfaces | util/SearchCriteria.java |
| Anonymous Classes | CLI menu handlers |

## Enabling Assertions

Assertions are used throughout the codebase for invariant checking:

### Enable at Runtime
```bash
java -ea edu.ccrm.cli.CCRMApplication
java -enableassertions edu.ccrm.cli.CCRMApplication
```

### Enable for Specific Package
```bash
java -ea:edu.ccrm... edu.ccrm.cli.CCRMApplication
```

### Sample Assertion Usage
```java
// In Student.java
assert credits >= 0 : "Credits cannot be negative: " + credits;
assert email.contains("@") : "Invalid email format: " + email;

// In Course.java
assert credits > 0 && credits <= 6 : "Course credits must be 1-6: " + credits;
```

## Sample Commands

### Import Data
```
Select option: 6 (Import/Export Data)
Select: 1 (Import Students)
File path: test-data/students.csv
```

### Enroll Student
```
Select option: 3 (Enrollment Management)
Select: 1 (Enroll Student)
Enter Student ID: STU001
Enter Course Code: CS101
```

### Generate Transcript
```
Select option: 1 (Student Management)
Select: 5 (Print Transcript)
Enter Student ID: STU001
```

### Backup Data
```
Select option: 7 (Backup & Utilities)
Select: 1 (Create Backup)
Backup created: backups/backup_2024-01-15_14-30-25/
```

## Project Features Demonstration

### Core Functionality
- ✅ Student CRUD operations with validation
- ✅ Course management with search/filter capabilities
- ✅ Enrollment with business rules (credit limits)
- ✅ Grade recording with GPA calculation
- ✅ Transcript generation with formatting

### File Operations
- ✅ CSV import/export with error handling
- ✅ Timestamped backup creation
- ✅ Recursive directory size calculation
- ✅ Path manipulation with NIO.2

### Advanced Java Features
- ✅ Stream API for data processing and reporting
- ✅ Lambda expressions for sorting and filtering
- ✅ Optional usage for null safety
- ✅ Date/Time API for timestamps
- ✅ Try-with-resources for file operations

## Test Data

Sample CSV files are provided in the `test-data/` directory:
- `students.csv` - Sample student records
- `courses.csv` - Sample course catalog
- `enrollments.csv` - Sample enrollment data

## Architecture Notes

The application follows a layered architecture:
- **CLI Layer** - User interface and menu handling
- **Service Layer** - Business logic and operations
- **Domain Layer** - Core entities and value objects
- **I/O Layer** - File operations and data persistence
- **Utility Layer** - Helper classes and common functionality

## Platform Summary

**Java SE (Standard Edition)** is the foundation platform used for this project, providing:
- Core language features and syntax
- Essential APIs (Collections, I/O, Concurrency)
- Desktop application capabilities
- Cross-platform compatibility through JVM

This differs from **Java ME** (constrained devices) and **Java EE** (enterprise web applications), making it ideal for desktop utilities and standalone applications like CCRM.

## Acknowledgments

- Oracle Java Documentation
- Effective Java by Joshua Bloch
- Clean Code principles by Robert C. Martin
- Java design patterns from Gang of Four

---
*This project demonstrates comprehensive Java SE knowledge including OOP principles, design patterns, modern I/O, functional programming, and best practices for enterprise-ready applications.*