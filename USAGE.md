# CCRM Usage Guide

## Quick Start

### Running the Application
```bash
# Compile
javac -d bin -sourcepath src src/edu/ccrm/cli/CCRMApplication.java

# Run with assertions enabled
java -ea -cp bin edu.ccrm.cli.CCRMApplication
```

## Menu Navigation

### Main Menu Options
```
=== Campus Course & Records Manager ===
1. Student Management
2. Course Management  
3. Enrollment Management
4. Grade Management
5. Reports & Analytics
6. Import/Export Data
7. Backup & Utilities
8. Exit
```

## Sample Operations

### 1. Add a New Student
```
Menu: 1 (Student Management)
Submenu: 1 (Add Student)
Enter details:
- Student ID: STU001
- Registration Number: REG2024001
- Full Name: John Smith
- Email: john.smith@university.edu
```

### 2. Create a Course
```
Menu: 2 (Course Management)
Submenu: 1 (Add Course)
Enter details:
- Course Code: CS101
- Title: Introduction to Computer Science
- Credits: 3
- Department: Computer Science
- Semester: FALL
```

### 3. Enroll Student in Course
```
Menu: 3 (Enrollment Management)
Submenu: 1 (Enroll Student)
- Student ID: STU001
- Course Code: CS101
System checks: Credit limit, prerequisites
```

### 4. Record Grades
```
Menu: 4 (Grade Management)
Submenu: 1 (Record Grade)
- Student ID: STU001
- Course Code: CS101
- Marks: 85
System calculates: Letter grade (A), GPA impact
```

### 5. Generate Reports
```
Menu: 5 (Reports & Analytics)
Options:
1. Student Transcript
2. Course Enrollment Report
3. GPA Distribution
4. Top Performers
```

### 6. Data Import/Export
```
Menu: 6 (Import/Export Data)
Submenu: 1 (Import Students)
File: test-data/students.csv
Format: ID,RegNo,Name,Email,Status
```

### 7. Backup Operations
```
Menu: 7 (Backup & Utilities)
Submenu: 1 (Create Backup)
Creates timestamped folder: backups/backup_2024-01-15_14-30-25/
Includes: students.csv, courses.csv, enrollments.csv, grades.csv
```

## Sample Data Files

### students.csv
```csv
ID,RegNo,Name,Email,Status
STU001,REG2024001,John Smith,john.smith@university.edu,ACTIVE
STU002,REG2024002,Jane Doe,jane.doe@university.edu,ACTIVE
STU003,REG2024003,Mike Johnson,mike.johnson@university.edu,ACTIVE
```

### courses.csv
```csv
Code,Title,Credits,Department,Semester,Instructor
CS101,Introduction to Computer Science,3,Computer Science,FALL,Dr. Anderson
MATH201,Calculus II,4,Mathematics,SPRING,Prof. Wilson
ENG101,English Composition,3,English,FALL,Dr. Brown
PHYS101,General Physics,4,Physics,SPRING,Prof. Taylor
```

### enrollments.csv
```csv
StudentID,CourseCode,EnrollmentDate,Status
STU001,CS101,2024-01-15,ENROLLED
STU001,MATH201,2024-01-15,ENROLLED
STU002,CS101,2024-01-16,ENROLLED
STU003,ENG101,2024-01-17,ENROLLED
```

## Business Rules

### Enrollment Rules
- Maximum 18 credits per semester
- Cannot enroll in same course twice
- Must be ACTIVE status to enroll
- Course must be available for enrollment

### Grading Rules
- Marks range: 0-100
- Grade calculation:
  - A: 90-100 (4.0 GPA)
  - B: 80-89 (3.0 GPA)
  - C: 70-79 (2.0 GPA)
  - D: 60-69 (1.0 GPA)
  - F: 0-59 (0.0 GPA)

### File Operations
- CSV format for import/export
- Automatic backup with timestamps
- Error handling for malformed data
- Validation during import process

## Error Handling Examples

### Common Errors and Solutions

**DuplicateEnrollmentException**
```
Error: Student STU001 is already enrolled in CS101
Solution: Check enrollment status before enrolling
```

**MaxCreditLimitExceededException**
```
Error: Adding CS101 (3 credits) would exceed 18-credit limit
Solution: Drop a course or wait for next semester
```

**InvalidGradeException**
```
Error: Marks 105 is outside valid range (0-100)
Solution: Enter marks between 0 and 100
```

**FileImportException**
```
Error: CSV file missing required column 'Email'
Solution: Check file format matches expected structure
```

## Advanced Features

### Search and Filter
- Students by name, email, or status
- Courses by instructor, department, or semester
- Enrollments by student or course
- Grades by GPA range or letter grade

### Reporting Capabilities
- Individual student transcripts
- Course-wise enrollment statistics
- Semester-wise GPA reports
- Department performance analysis

### Backup and Recovery
- Automatic timestamped backups
- Recursive directory size calculation
- Selective data export options
- Backup integrity verification

## Tips for Best Experience

1. **Always enable assertions** (`-ea` flag) for runtime validation
2. **Import sample data** first to explore features
3. **Create backups** before major operations
4. **Check file permissions** for import/export operations
5. **Validate data** before importing large datasets

## Troubleshooting

### Common Issues

**ClassNotFoundException**
- Ensure classpath includes 'bin' directory
- Verify all .class files are compiled

**FileNotFoundException**
- Check file paths are relative to project root
- Ensure test-data directory exists

**NumberFormatException**
- Verify CSV files have correct numeric formats
- Check for extra spaces or invalid characters

**OutOfMemoryError**
- Reduce dataset size for testing
- Increase JVM heap size: `-Xmx512m`