# CCRM-Project
A comprehensive, console-based Java application designed to simulate the core operations of a university's academic records system. Manage students, courses, enrollments, and grades through an intuitive terminal interface.
# Campus Course & Records Manager (CCRM) 🎓

A comprehensive, console-based Java application designed to simulate the core operations of a university's academic records system. Manage students, courses, enrollments, and grades through an intuitive terminal interface.

[![Java](https://img.shields.io/badge/Java-17+-ED8B00?style=flat&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Maintenance](https://img.shields.io/badge/Maintained%3F-yes-green.svg)](https://github.com/your-github-username/CCRM/graphs/commit-activity)

---

## ✨ Features

- **Student Management**: Create, view, update, and delete student profiles.
- **Course Catalog**: Maintain a full catalog of courses with details like credits, instructor, and schedule.
- **Enrollment System**: Enroll and drop students from courses with validation (e.g., credit limits).
- **Grading & Transcripts**: Record grades and generate detailed, formatted academic transcripts.
- **Data Persistence**: Import/export data to/from CSV files for easy data management.
- **Backup & Restore**: Create timestamped backups of all application data.
- **Advanced Reporting**: Generate various reports using Java Streams API for filtering and aggregation.

## 🛠️ Built With

- **Java SE 17** (LTS) - Leveraging modern language features.
- **Object-Oriented Principles** - Full encapsulation, inheritance, abstraction, and polymorphism.
- **Design Patterns** - Singleton, Builder, and Strategy patterns for clean architecture.
- **NIO.2 API** - Modern file I/O operations for imports, exports, and backups.
- **Java Streams API & Lambdas** - For efficient data processing and functional programming.

## 📦 Project Structure
src/
├── edu/ccrm/
│ ├── cli/ # Command Line Interface and menus
│ ├── domain/ # Core entities (Student, Course, Enrollment)
│ ├── service/ # Business logic layer
│ ├── io/ # File I/O operations (CSV, Backup)
│ ├── util/ # Helper classes and utilities
│ └── config/ # Application configuration
└── test-data/ # Sample CSV files for testing


## 🚀 Getting Started

### Prerequisites

- **JDK 17 or higher** ([Download from Oracle](https://www.oracle.com/java/technologies/downloads/))
- A terminal or an IDE like Eclipse, IntelliJ IDEA, or VS Code.

### Installation & Run

1.  **Clone the repository**
    
2.  **Compile the project**
    ```bash
    javac -d bin -sourcepath src src/edu/ccrm/cli/CCRMApplication.java
    ```

3.  **Run the application**
    ```bash
    java -cp bin edu.ccrm.cli.CCRMApplication
    ```
    *For better error checking, run with assertions enabled:*
    ```bash
    java -ea -cp bin edu.ccrm.cli.CCRMApplication
    ```

### Running in an IDE (Eclipse/IntelliJ)
1.  Import the project as a **Java Project**.
2.  Ensure the JDK is set to version 17+.
3.  Locate the main class: `edu.ccrm.cli.CCRMApplication`.
4.  Run it!

## 📖 How to Use

1.  **Start with Sample Data**: Use the `Import/Export` menu to load the sample `students.csv` and `courses.csv` files from the `test-data/` directory.
2.  **Explore the Menus**:
    - **Student Management**: Add new students or view existing ones.
    - **Course Management**: Browse the course catalog.
    - **Enrollment Management**: Enroll a student in a course.
    - **Grading**: Assign grades to enrolled students.
    - **Import/Export**: Load or save your data.
    - **Backup & Utilities**: Create a backup of your current data.

## 🧪 Sample Data

The `/test-data` directory contains CSV files to populate the application:
- `students.csv`: Pre-defined list of students.
- `courses.csv`: Sample course catalog.
- `enrollments.csv`: Pre-existing enrollments (optional).

## 🧩 Design Highlights

- **Layered Architecture**: Clear separation between UI, business logic, and data layers.
- **Immutable Objects**: Heavy use of the Builder pattern for creating robust domain objects.
- **Exception Handling**: A custom hierarchy of exceptions for precise error handling.
- **Modern Java**: Demonstrates effective use of `Records`, `Optional`, `Streams`, `LocalDateTime`, and `try-with-resources`.

## 🤝 Contributing

Contributions are what make the open-source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.
1.  Fork the Project
2.  Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3.  Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4.  Push to the Branch (`git push origin feature/AmazingFeature`)
5.  Open a Pull Request

## 📜 License

Distributed under the MIT License. See `LICENSE` for more information.

## 📞 Contact

Your Name - Harshvardhan Santosh Magar (harshvardhan.24bce10079@vitbhopal.ac.in)

Project Link: [https://github.com/Harshvardhan-M/CCRM-Project](https://github.com/Harshvardhan-M/CCRM-Project.git)
