package edu.ccrm.cli;

import edu.ccrm.config.AppConfig;
import edu.ccrm.domain.*;
import edu.ccrm.domain.exceptions.*;
import edu.ccrm.service.*;
import edu.ccrm.io.*;
import edu.ccrm.util.Validators;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Main CLI application for Campus Course & Records Manager
 * Demonstrates console-based menu system with comprehensive functionality
 */
public class CCRMApplication {
    
    // Service layer dependencies
    private final StudentService studentService = new StudentServiceImpl();
    private final CourseService courseService = new CourseServiceImpl();
    private final EnrollmentService enrollmentService = new EnrollmentServiceImpl();
    private final GradeService gradeService = new GradeServiceImpl();
    private final TranscriptService transcriptService = new TranscriptServiceImpl();
    private final ImportExportService importExportService = new ImportExportService();
    private final BackupService backupService = new BackupService();
    
    // Configuration and utilities
    private final AppConfig config = AppConfig.getInstance();
    private final Scanner scanner = new Scanner(System.in);
    private boolean running = true;
    
    public static void main(String[] args) {
        System.out.println("Starting Campus Course & Records Manager...");
        System.out.println("Java Platform: " + System.getProperty("java.version"));
        System.out.println("OS: " + System.getProperty("os.name"));
        System.out.println();
        
        CCRMApplication app = new CCRMApplication();
        app.initializeSampleData();
        app.run();
    }
    
    /**
     * Main application loop with menu system
     */
    public void run() {
        printWelcomeMessage();
        
        while (running) {
            try {
                displayMainMenu();
                int choice = getIntInput("Enter your choice: ");
                
                switch (choice) {
                    case 1 -> handleStudentManagement();
                    case 2 -> handleCourseManagement();
                    case 3 -> handleEnrollmentManagement();
                    case 4 -> handleGradeManagement();
                    case 5 -> handleReportsAndAnalytics();
                    case 6 -> handleImportExport();
                    case 7 -> handleBackupUtilities();
                    case 8 -> {
                        System.out.println("Thank you for using CCRM!");
                        running = false;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
                
                if (running) {
                    System.out.println("\nPress Enter to continue...");
                    scanner.nextLine();
                }
                
            } catch (Exception e) {
                System.err.println("An error occurred: " + e.getMessage());
                if (e instanceof CCRMException) {
                    System.err.println("Error Code: " + ((CCRMException) e).getErrorCode());
                }
            }
        }
        
        scanner.close();
    }
    
    private void printWelcomeMessage() {
        System.out.println("=".repeat(60));
        System.out.println(config.getApplicationInfo());
        System.out.println("=".repeat(60));
        System.out.println();
        
        // Platform information
        System.out.println("Java Platform Summary:");
        System.out.println("• Java SE: Standard Edition for desktop applications");
        System.out.println("• Java ME: Micro Edition for embedded/mobile devices");
        System.out.println("• Java EE: Enterprise Edition for web/server applications");
        System.out.println("This application uses Java SE for comprehensive desktop functionality.");
        System.out.println();
    }
    
    private void displayMainMenu() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("   Campus Course & Records Manager");
        System.out.println("=".repeat(40));
        System.out.println("1. Student Management");
        System.out.println("2. Course Management");
        System.out.println("3. Enrollment Management");
        System.out.println("4. Grade Management");
        System.out.println("5. Reports & Analytics");
        System.out.println("6. Import/Export Data");
        System.out.println("7. Backup & Utilities");
        System.out.println("8. Exit");
        System.out.println("=".repeat(40));
    }
    
    // Student Management Methods
    private void handleStudentManagement() {
        System.out.println("\n--- Student Management ---");
        System.out.println("1. Add Student");
        System.out.println("2. List All Students");
        System.out.println("3. Search Students");
        System.out.println("4. Update Student");
        System.out.println("5. Print Student Transcript");
        System.out.println("6. Deactivate Student");
        
        int choice = getIntInput("Enter choice: ");
        
        switch (choice) {
            case 1 -> addStudent();
            case 2 -> listAllStudents();
            case 3 -> searchStudents();
            case 4 -> updateStudent();
            case 5 -> printStudentTranscript();
            case 6 -> deactivateStudent();
            default -> System.out.println("Invalid choice.");
        }
    }
    
    private void addStudent() {
        try {
            System.out.println("\n--- Add New Student ---");
            
            String id = getStringInput("Student ID (e.g., STU001): ");
            if (!Validators.isValidStudentId(id)) {
                System.out.println("Invalid student ID format. Use format: STU###");
                return;
            }
            
            String regNo = getStringInput("Registration Number (e.g., REG2024001): ");
            if (!Validators.isValidRegNo(regNo)) {
                System.out.println("Invalid registration number format. Use format: REG#######");
                return;
            }
            
            String fullName = getStringInput("Full Name: ");
            if (!Validators.isValidName(fullName)) {
                System.out.println("Invalid name format.");
                return;
            }
            
            String email = getStringInput("Email: ");
            if (!Validators.isValidEmail(email)) {
                System.out.println("Invalid email format.");
                return;
            }
            
            Student student = new Student.Builder()
                .setId(id)
                .setRegNo(regNo)
                .setFullName(fullName)
                .setEmail(email)
                .setStatus(StudentStatus.ACTIVE)
                .setEnrollmentDate(LocalDateTime.now())
                .build();
            
            studentService.addStudent(student);
            System.out.println("Student added successfully!");
            System.out.println(student);
            
        } catch (DuplicateEntityException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error adding student: " + e.getMessage());
        }
    }
    
    private void listAllStudents() {
        System.out.println("\n--- All Students ---");
        List<Student> students = studentService.getAllStudents();
        
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        
        students.stream()
            .sorted((s1, s2) -> s1.getFullName().compareToIgnoreCase(s2.getFullName()))
            .forEach(student -> {
                System.out.printf("%-8s %-12s %-25s %-30s %s%n",
                    student.getId(),
                    student.getRegNo(),
                    student.getFullName(),
                    student.getEmail(),
                    student.getStatus());
            });
        
        System.out.println("\nTotal students: " + students.size());
    }
    
    private void searchStudents() {
        System.out.println("\n--- Search Students ---");
        System.out.println("1. Search by Name");
        System.out.println("2. Search by Email");
        System.out.println("3. Filter by Status");
        
        int choice = getIntInput("Enter choice: ");
        
        switch (choice) {
            case 1 -> {
                String namePart = getStringInput("Enter name to search: ");
                List<Student> results = studentService.searchStudentsByName(namePart);
                displayStudentSearchResults(results);
            }
            case 2 -> {
                String emailPart = getStringInput("Enter email to search: ");
                List<Student> results = studentService.searchStudentsByEmail(emailPart);
                displayStudentSearchResults(results);
            }
            case 3 -> {
                System.out.println("Available statuses: " + Arrays.toString(StudentStatus.values()));
                String statusStr = getStringInput("Enter status: ");
                try {
                    StudentStatus status = StudentStatus.valueOf(statusStr.toUpperCase());
                    List<Student> results = studentService.getStudentsByStatus(status);
                    displayStudentSearchResults(results);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid status.");
                }
            }
            default -> System.out.println("Invalid choice.");
        }
    }
    
    private void displayStudentSearchResults(List<Student> students) {
        if (students.isEmpty()) {
            System.out.println("No students found matching criteria.");
            return;
        }
        
        System.out.println("\nSearch Results:");
        System.out.println("-".repeat(80));
        students.forEach(System.out::println);
        System.out.println("Found " + students.size() + " student(s).");
    }
    
    private void updateStudent() {
        String id = getStringInput("Enter Student ID to update: ");
        
        try {
            Optional<Student> studentOpt = studentService.getStudentById(id);
            if (studentOpt.isEmpty()) {
                System.out.println("Student not found.");
                return;
            }
            
            Student student = studentOpt.get();
            System.out.println("Current student information:");
            System.out.println(student);
            
            System.out.println("\nWhat would you like to update?");
            System.out.println("1. Full Name");
            System.out.println("2. Email");
            System.out.println("3. Status");
            
            int choice = getIntInput("Enter choice: ");
            
            switch (choice) {
                case 1 -> {
                    String newName = getStringInput("Enter new full name: ");
                    if (Validators.isValidName(newName)) {
                        student.setFullName(newName);
                        studentService.updateStudent(student);
                        System.out.println("Name updated successfully!");
                    } else {
                        System.out.println("Invalid name format.");
                    }
                }
                case 2 -> {
                    String newEmail = getStringInput("Enter new email: ");
                    if (Validators.isValidEmail(newEmail)) {
                        student.setEmail(newEmail);
                        studentService.updateStudent(student);
                        System.out.println("Email updated successfully!");
                    } else {
                        System.out.println("Invalid email format.");
                    }
                }
                case 3 -> {
                    System.out.println("Available statuses: " + Arrays.toString(StudentStatus.values()));
                    String statusStr = getStringInput("Enter new status: ");
                    try {
                        StudentStatus status = StudentStatus.valueOf(statusStr.toUpperCase());
                        student.setStatus(status);
                        studentService.updateStudent(student);
                        System.out.println("Status updated successfully!");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid status.");
                    }
                }
                default -> System.out.println("Invalid choice.");
            }
            
        } catch (EntityNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private void printStudentTranscript() {
        String studentId = getStringInput("Enter Student ID: ");
        
        try {
            String transcript = transcriptService.generateTranscriptReport(studentId);
            System.out.println("\n" + transcript);
            
            String saveChoice = getStringInput("Save transcript to file? (y/n): ");
            if (saveChoice.toLowerCase().startsWith("y")) {
                String filename = "transcript_" + studentId + "_" + 
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt";
                transcriptService.exportTranscriptToPDF(studentId, filename);
                System.out.println("Transcript saved to: " + filename);
            }
            
        } catch (EntityNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private void deactivateStudent() {
        String id = getStringInput("Enter Student ID to deactivate: ");
        
        try {
            studentService.deactivateStudent(id);
            System.out.println("Student deactivated successfully!");
        } catch (EntityNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    // Course Management Methods
    private void handleCourseManagement() {
        System.out.println("\n--- Course Management ---");
        System.out.println("1. Add Course");
        System.out.println("2. List All Courses");
        System.out.println("3. Search Courses");
        System.out.println("4. Update Course");
        System.out.println("5. Assign Instructor");
        System.out.println("6. Deactivate Course");
        
        int choice = getIntInput("Enter choice: ");
        
        switch (choice) {
            case 1 -> addCourse();
            case 2 -> listAllCourses();
            case 3 -> searchCourses();
            case 4 -> updateCourse();
            case 5 -> assignInstructor();
            case 6 -> deactivateCourse();
            default -> System.out.println("Invalid choice.");
        }
    }
    
    private void addCourse() {
        try {
            System.out.println("\n--- Add New Course ---");
            
            String code = getStringInput("Course Code (e.g., CS101): ");
            if (!Validators.isValidCourseCode(code)) {
                System.out.println("Invalid course code format. Use format: XX###");
                return;
            }
            
            String title = getStringInput("Course Title: ");
            if (title.trim().isEmpty()) {
                System.out.println("Course title cannot be empty.");
                return;
            }
            
            int credits = getIntInput("Credits (1-6): ");
            if (!Validators.isValidCredits(credits)) {
                System.out.println("Credits must be between 1 and 6.");
                return;
            }
            
            String department = getStringInput("Department: ");
            if (department.trim().isEmpty()) {
                System.out.println("Department cannot be empty.");
                return;
            }
            
            System.out.println("Available semesters: " + Arrays.toString(Semester.values()));
            String semesterStr = getStringInput("Semester: ");
            Semester semester;
            try {
                semester = Semester.valueOf(semesterStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid semester.");
                return;
            }
            
            String instructor = getStringInput("Instructor (optional): ");
            
            Course course = new Course.Builder(code, title)
                .credits(credits)
                .department(department)
                .semester(semester)
                .instructor(instructor.trim().isEmpty() ? null : instructor)
                .build();
            
            courseService.addCourse(course);
            System.out.println("Course added successfully!");
            System.out.println(course);
            
        } catch (DuplicateEntityException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error adding course: " + e.getMessage());
        }
    }
    
    private void listAllCourses() {
        System.out.println("\n--- All Courses ---");
        List<Course> courses = courseService.getAllCourses();
        
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }
        
        System.out.printf("%-8s %-30s %-7s %-20s %-10s %-20s%n",
            "Code", "Title", "Credits", "Department", "Semester", "Instructor");
        System.out.println("-".repeat(100));
        
        courses.stream()
            .sorted((c1, c2) -> c1.getCode().compareToIgnoreCase(c2.getCode()))
            .forEach(course -> {
                System.out.printf("%-8s %-30s %-7d %-20s %-10s %-20s%n",
                    course.getCode(),
                    course.getTitle().length() > 30 ? course.getTitle().substring(0, 27) + "..." : course.getTitle(),
                    course.getCredits(),
                    course.getDepartment(),
                    course.getSemester() != null ? course.getSemester() : "N/A",
                    course.getInstructor() != null ? course.getInstructor() : "Unassigned");
            });
        
        System.out.println("\nTotal courses: " + courses.size());
    }
    
    private void searchCourses() {
        System.out.println("\n--- Search Courses ---");
        System.out.println("1. Search by Department");
        System.out.println("2. Search by Semester");
        System.out.println("3. Search by Instructor");
        System.out.println("4. Search by Credits");
        
        int choice = getIntInput("Enter choice: ");
        
        switch (choice) {
            case 1 -> {
                String department = getStringInput("Enter department: ");
                List<Course> results = courseService.getCoursesByDepartment(department);
                displayCourseSearchResults(results);
            }
            case 2 -> {
                System.out.println("Available semesters: " + Arrays.toString(Semester.values()));
                String semesterStr = getStringInput("Enter semester: ");
                try {
                    Semester semester = Semester.valueOf(semesterStr.toUpperCase());
                    List<Course> results = courseService.getCoursesBySemester(semester);
                    displayCourseSearchResults(results);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid semester.");
                }
            }
            case 3 -> {
                String instructor = getStringInput("Enter instructor name: ");
                List<Course> results = courseService.getCoursesByInstructor(instructor);
                displayCourseSearchResults(results);
            }
            case 4 -> {
                int credits = getIntInput("Enter credits: ");
                List<Course> results = courseService.getCoursesByCredits(credits);
                displayCourseSearchResults(results);
            }
            default -> System.out.println("Invalid choice.");
        }
    }
    
    private void displayCourseSearchResults(List<Course> courses) {
        if (courses.isEmpty()) {
            System.out.println("No courses found matching criteria.");
            return;
        }
        
        System.out.println("\nSearch Results:");
        System.out.println("-".repeat(80));
        courses.forEach(System.out::println);
        System.out.println("Found " + courses.size() + " course(s).");
    }
    
    private void updateCourse() {
        String code = getStringInput("Enter Course Code to update: ");
        
        try {
            Optional<Course> courseOpt = courseService.getCourseByCode(code);
            if (courseOpt.isEmpty()) {
                System.out.println("Course not found.");
                return;
            }
            
            Course course = courseOpt.get();
            System.out.println("Current course information:");
            System.out.println(course);
            
            System.out.println("\nWhat would you like to update?");
            System.out.println("1. Title");
            System.out.println("2. Credits");
            System.out.println("3. Department");
            System.out.println("4. Semester");
            
            int choice = getIntInput("Enter choice: ");
            
            switch (choice) {
                case 1 -> {
                    String newTitle = getStringInput("Enter new title: ");
                    if (!newTitle.trim().isEmpty()) {
                        course.setTitle(newTitle);
                        courseService.updateCourse(course);
                        System.out.println("Title updated successfully!");
                    } else {
                        System.out.println("Title cannot be empty.");
                    }
                }
                case 2 -> {
                    int newCredits = getIntInput("Enter new credits (1-6): ");
                    if (Validators.isValidCredits(newCredits)) {
                        course.setCredits(newCredits);
                        courseService.updateCourse(course);
                        System.out.println("Credits updated successfully!");
                    } else {
                        System.out.println("Credits must be between 1 and 6.");
                    }
                }
                case 3 -> {
                    String newDepartment = getStringInput("Enter new department: ");
                    if (!newDepartment.trim().isEmpty()) {
                        course.setDepartment(newDepartment);
                        courseService.updateCourse(course);
                        System.out.println("Department updated successfully!");
                    } else {
                        System.out.println("Department cannot be empty.");
                    }
                }
                case 4 -> {
                    System.out.println("Available semesters: " + Arrays.toString(Semester.values()));
                    String semesterStr = getStringInput("Enter new semester: ");
                    try {
                        Semester semester = Semester.valueOf(semesterStr.toUpperCase());
                        course.setSemester(semester);
                        courseService.updateCourse(course);
                        System.out.println("Semester updated successfully!");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid semester.");
                    }
                }
                default -> System.out.println("Invalid choice.");
            }
            
        } catch (EntityNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private void assignInstructor() {
        String courseCode = getStringInput("Enter Course Code: ");
        String instructor = getStringInput("Enter Instructor Name: ");
        
        try {
            courseService.assignInstructor(courseCode, instructor);
            System.out.println("Instructor assigned successfully!");
        } catch (EntityNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private void deactivateCourse() {
        String code = getStringInput("Enter Course Code to deactivate: ");
        
        try {
            courseService.deactivateCourse(code);
            System.out.println("Course deactivated successfully!");
        } catch (EntityNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    // Enrollment Management Methods
    private void handleEnrollmentManagement() {
        System.out.println("\n--- Enrollment Management ---");
        System.out.println("1. Enroll Student");
        System.out.println("2. Unenroll Student");
        System.out.println("3. View Student Enrollments");
        System.out.println("4. View Course Enrollments");
        System.out.println("5. Check Enrollment Status");
        
        int choice = getIntInput("Enter choice: ");
        
        switch (choice) {
            case 1 -> enrollStudent();
            case 2 -> unenrollStudent();
            case 3 -> viewStudentEnrollments();
            case 4 -> viewCourseEnrollments();
            case 5 -> checkEnrollmentStatus();
            default -> System.out.println("Invalid choice.");
        }
    }
    
    private void enrollStudent() {
        String studentId = getStringInput("Enter Student ID: ");
        String courseCode = getStringInput("Enter Course Code: ");
        
        try {
            enrollmentService.enrollStudent(studentId, courseCode);
            System.out.println("Student enrolled successfully!");
            
            // Display updated credit count
            int credits = enrollmentService.getStudentCreditCount(studentId);
            System.out.println("Student's total credits: " + credits);
            
        } catch (DuplicateEnrollmentException | MaxCreditLimitExceededException | EntityNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private void unenrollStudent() {
        String studentId = getStringInput("Enter Student ID: ");
        String courseCode = getStringInput("Enter Course Code: ");
        
        try {
            enrollmentService.unenrollStudent(studentId, courseCode);
            System.out.println("Student unenrolled successfully!");
            
            // Display updated credit count
            int credits = enrollmentService.getStudentCreditCount(studentId);
            System.out.println("Student's total credits: " + credits);
            
        } catch (EntityNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private void viewStudentEnrollments() {
        String studentId = getStringInput("Enter Student ID: ");
        
        try {
            List<Enrollment> enrollments = enrollmentService.getStudentEnrollments(studentId);
            
            if (enrollments.isEmpty()) {
                System.out.println("No enrollments found for student.");
                return;
            }
            
            System.out.println("\nStudent Enrollments:");
            System.out.println("-".repeat(60));
            enrollments.forEach(enrollment -> {
                System.out.printf("Course: %-8s | Date: %-10s | Status: %s%n",
                    enrollment.getCourseCode(),
                    enrollment.getEnrollmentDate().toLocalDate(),
                    enrollment.getStatus());
            });
            
            int totalCredits = enrollmentService.getStudentCreditCount(studentId);
            System.out.println("\nTotal Credits: " + totalCredits);
            
        } catch (EntityNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private void viewCourseEnrollments() {
        String courseCode = getStringInput("Enter Course Code: ");
        
        try {
            List<Enrollment> enrollments = enrollmentService.getCourseEnrollments(courseCode);
            
            if (enrollments.isEmpty()) {
                System.out.println("No enrollments found for course.");
                return;
            }
            
            System.out.println("\nCourse Enrollments:");
            System.out.println("-".repeat(60));
            enrollments.forEach(enrollment -> {
                System.out.printf("Student: %-8s | Date: %-10s | Status: %s%n",
                    enrollment.getStudentId(),
                    enrollment.getEnrollmentDate().toLocalDate(),
                    enrollment.getStatus());
            });
            
            System.out.println("\nTotal Enrollments: " + enrollments.size());
            
        } catch (EntityNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private void checkEnrollmentStatus() {
        String studentId = getStringInput("Enter Student ID: ");
        String courseCode = getStringInput("Enter Course Code: ");
        
        boolean isEnrolled = enrollmentService.isStudentEnrolled(studentId, courseCode);
        
        if (isEnrolled) {
            System.out.println("Student is enrolled in the course.");
        } else {
            System.out.println("Student is NOT enrolled in the course.");
        }
    }
    
    // Grade Management Methods
    private void handleGradeManagement() {
        System.out.println("\n--- Grade Management ---");
        System.out.println("1. Record Grade");
        System.out.println("2. Update Grade");
        System.out.println("3. View Student Grades");
        System.out.println("4. View Course Grades");
        System.out.println("5. Calculate Student GPA");
        System.out.println("6. Calculate Course Average");
        
        int choice = getIntInput("Enter choice: ");
        
        switch (choice) {
            case 1 -> recordGrade();
            case 2 -> updateGrade();
            case 3 -> viewStudentGrades();
            case 4 -> viewCourseGrades();
            case 5 -> calculateStudentGPA();
            case 6 -> calculateCourseAverage();
            default -> System.out.println("Invalid choice.");
        }
    }
    
    private void recordGrade() {
        String studentId = getStringInput("Enter Student ID: ");
        String courseCode = getStringInput("Enter Course Code: ");
        
        try {
            double marks = getDoubleInput("Enter marks (0-100): ");
            
            if (!Validators.isValidMarks(marks)) {
                System.out.println("Marks must be between 0 and 100.");
                return;
            }
            
            Grade grade = gradeService.recordGrade(studentId, courseCode, marks);
            System.out.println("Grade recorded successfully!");
            System.out.println(grade);
            
        } catch (InvalidGradeException | EntityNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private void updateGrade() {
        String studentId = getStringInput("Enter Student ID: ");
        String courseCode = getStringInput("Enter Course Code: ");
        
        try {
            double newMarks = getDoubleInput("Enter new marks (0-100): ");
            
            if (!Validators.isValidMarks(newMarks)) {
                System.out.println("Marks must be between 0 and 100.");
                return;
            }
            
            Grade grade = gradeService.updateGrade(studentId, courseCode, newMarks);
            System.out.println("Grade updated successfully!");
            System.out.println(grade);
            
        } catch (InvalidGradeException | EntityNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private void viewStudentGrades() {
        String studentId = getStringInput("Enter Student ID: ");
        
        try {
            List<Grade> grades = gradeService.getStudentGrades(studentId);
            
            if (grades.isEmpty()) {
                System.out.println("No grades found for student.");
                return;
            }
            
            System.out.println("\nStudent Grades:");
            System.out.println("-".repeat(70));
            System.out.printf("%-10s %-8s %-12s %-8s%n", "Course", "Marks", "Letter Grade", "Points");
            System.out.println("-".repeat(70));
            
            grades.forEach(grade -> {
                System.out.printf("%-10s %-8.1f %-12s %-8.2f%n",
                    grade.getCourseCode(),
                    grade.getMarks(),
                    grade.getLetterGrade(),
                    grade.getGradePoints());
            });
            
            double gpa = gradeService.calculateGPA(studentId);
            System.out.println("\nCurrent GPA: " + String.format("%.2f", gpa));
            
        } catch (EntityNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private void viewCourseGrades() {
        String courseCode = getStringInput("Enter Course Code: ");
        
        try {
            List<Grade> grades = gradeService.getCourseGrades(courseCode);
            
            if (grades.isEmpty()) {
                System.out.println("No grades found for course.");
                return;
            }
            
            System.out.println("\nCourse Grades:");
            System.out.println("-".repeat(70));
            System.out.printf("%-10s %-8s %-12s %-8s%n", "Student", "Marks", "Letter Grade", "Points");
            System.out.println("-".repeat(70));
            
            grades.forEach(grade -> {
                System.out.printf("%-10s %-8.1f %-12s %-8.2f%n",
                    grade.getStudentId(),
                    grade.getMarks(),
                    grade.getLetterGrade(),
                    grade.getGradePoints());
            });
            
            double average = gradeService.calculateCourseAverage(courseCode);
            System.out.println("\nCourse Average: " + String.format("%.2f", average));
            
        } catch (EntityNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private void calculateStudentGPA() {
        String studentId = getStringInput("Enter Student ID: ");
        
        try {
            double gpa = gradeService.calculateGPA(studentId);
            System.out.println("Student GPA: " + String.format("%.2f", gpa));
            
            // Show GPA interpretation
            if (gpa >= 3.5) {
                System.out.println("Academic Standing: Excellent (Dean's List)");
            } else if (gpa >= 3.0) {
                System.out.println("Academic Standing: Good");
            } else if (gpa >= 2.0) {
                System.out.println("Academic Standing: Satisfactory");
            } else if (gpa >= 1.0) {
                System.out.println("Academic Standing: Poor (Academic Warning)");
            } else {
                System.out.println("Academic Standing: Failing (Academic Probation)");
            }
            
        } catch (EntityNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private void calculateCourseAverage() {
        String courseCode = getStringInput("Enter Course Code: ");
        
        try {
            double average = gradeService.calculateCourseAverage(courseCode);
            System.out.println("Course Average: " + String.format("%.2f", average));
            
            // Show grade interpretation
            LetterGrade letterGrade = LetterGrade.fromMarks(average);
            System.out.println("Course Letter Grade: " + letterGrade + " (" + letterGrade.getDescription() + ")");
            
        } catch (EntityNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    // Reports and Analytics Methods
    private void handleReportsAndAnalytics() {
        System.out.println("\n--- Reports & Analytics ---");
        System.out.println("1. Student Statistics");
        System.out.println("2. Course Statistics");
        System.out.println("3. Enrollment Statistics");
        System.out.println("4. Grade Distribution");
        System.out.println("5. Top Performing Students");
        System.out.println("6. Department Analysis");
        
        int choice = getIntInput("Enter choice: ");
        
        switch (choice) {
            case 1 -> showStudentStatistics();
            case 2 -> showCourseStatistics();
            case 3 -> showEnrollmentStatistics();
            case 4 -> showGradeDistribution();
            case 5 -> showTopPerformingStudents();
            case 6 -> showDepartmentAnalysis();
            default -> System.out.println("Invalid choice.");
        }
    }
    
    private void showStudentStatistics() {
        System.out.println("\n--- Student Statistics ---");
        
        List<Student> allStudents = studentService.getAllStudents();
        System.out.println("Total Students: " + allStudents.size());
        
        // Group by status using streams
        Map<StudentStatus, Long> statusCounts = allStudents.stream()
            .collect(Collectors.groupingBy(Student::getStatus, Collectors.counting()));
        
        System.out.println("\nBy Status:");
        statusCounts.forEach((status, count) -> 
            System.out.println("  " + status + ": " + count));
        
        // GPA statistics
        OptionalDouble avgGPA = allStudents.stream()
            .mapToDouble(Student::getGpa)
            .average();
        
        if (avgGPA.isPresent()) {
            System.out.println("\nGPA Statistics:");
            System.out.println("  Average GPA: " + String.format("%.2f", avgGPA.getAsDouble()));
            
            double maxGPA = allStudents.stream()
                .mapToDouble(Student::getGpa)
                .max()
                .orElse(0.0);
            System.out.println("  Highest GPA: " + String.format("%.2f", maxGPA));
        }
    }
    
    private void showCourseStatistics() {
        System.out.println("\n--- Course Statistics ---");
        
        List<Course> allCourses = courseService.getAllCourses();
        System.out.println("Total Courses: " + allCourses.size());
        
        // Group by department
        Map<String, Long> deptCounts = allCourses.stream()
            .collect(Collectors.groupingBy(Course::getDepartment, Collectors.counting()));
        
        System.out.println("\nBy Department:");
        deptCounts.entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .forEach(entry -> 
                System.out.println("  " + entry.getKey() + ": " + entry.getValue()));
        
        // Group by semester
        Map<Semester, Long> semesterCounts = allCourses.stream()
            .filter(course -> course.getSemester() != null)
            .collect(Collectors.groupingBy(Course::getSemester, Collectors.counting()));
        
        System.out.println("\nBy Semester:");
        semesterCounts.forEach((semester, count) -> 
            System.out.println("  " + semester + ": " + count));
    }
    
    private void showEnrollmentStatistics() {
        System.out.println("\n--- Enrollment Statistics ---");
        
        List<Enrollment> allEnrollments = enrollmentService.getAllEnrollments();
        System.out.println("Total Enrollments: " + allEnrollments.size());
        
        // Group by status
        Map<Enrollment.EnrollmentStatus, Long> statusCounts = allEnrollments.stream()
            .collect(Collectors.groupingBy(Enrollment::getStatus, Collectors.counting()));
        
        System.out.println("\nBy Status:");
        statusCounts.forEach((status, count) -> 
            System.out.println("  " + status + ": " + count));
        
        // Most popular courses
        Map<String, Long> courseCounts = allEnrollments.stream()
            .collect(Collectors.groupingBy(Enrollment::getCourseCode, Collectors.counting()));
        
        System.out.println("\nMost Popular Courses:");
        courseCounts.entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .limit(5)
            .forEach(entry -> 
                System.out.println("  " + entry.getKey() + ": " + entry.getValue() + " enrollments"));
    }
    
    private void showGradeDistribution() {
        System.out.println("\n--- Grade Distribution ---");
        
        List<Grade> allGrades = gradeService.getAllGrades();
        
        if (allGrades.isEmpty()) {
            System.out.println("No grades recorded yet.");
            return;
        }
        
        // Group by letter grade
        Map<LetterGrade, Long> gradeCounts = allGrades.stream()
            .collect(Collectors.groupingBy(Grade::getLetterGrade, Collectors.counting()));
        
        System.out.println("Total Grades: " + allGrades.size());
        System.out.println("\nDistribution:");
        
        Arrays.stream(LetterGrade.values())
            .forEach(grade -> {
                long count = gradeCounts.getOrDefault(grade, 0L);
                double percentage = (count * 100.0) / allGrades.size();
                System.out.printf("  %s: %d (%.1f%%)%n", grade, count, percentage);
            });
        
        // Pass rate
        long passingGrades = allGrades.stream()
            .filter(grade -> grade.getLetterGrade().isPassing())
            .count();
        double passRate = (passingGrades * 100.0) / allGrades.size();
        System.out.println("\nPass Rate: " + String.format("%.1f%%", passRate));
    }
    
    private void showTopPerformingStudents() {
        System.out.println("\n--- Top Performing Students ---");
        
        List<Student> students = studentService.getAllStudents();
        
        List<Student> topStudents = students.stream()
            .filter(student -> student.getGpa() > 0)
            .sorted((s1, s2) -> Double.compare(s2.getGpa(), s1.getGpa()))
            .limit(10)
            .collect(Collectors.toList());
        
        if (topStudents.isEmpty()) {
            System.out.println("No students with recorded GPAs.");
            return;
        }
        
        System.out.printf("%-10s %-25s %-8s %-8s%n", "ID", "Name", "GPA", "Credits");
        System.out.println("-".repeat(60));
        
        topStudents.forEach(student -> {
            System.out.printf("%-10s %-25s %-8.2f %-8d%n",
                student.getId(),
                student.getFullName().length() > 25 ? 
                    student.getFullName().substring(0, 22) + "..." : student.getFullName(),
                student.getGpa(),
                student.getTotalCredits());
        });
    }
    
    private void showDepartmentAnalysis() {
        System.out.println("\n--- Department Analysis ---");
        
        List<Course> courses = courseService.getAllCourses();
        
        Map<String, List<Course>> coursesByDept = courses.stream()
            .collect(Collectors.groupingBy(Course::getDepartment));
        
        coursesByDept.forEach((dept, deptCourses) -> {
            System.out.println("\n" + dept + ":");
            System.out.println("  Total Courses: " + deptCourses.size());
            
            int totalCredits = deptCourses.stream()
                .mapToInt(Course::getCredits)
                .sum();
            System.out.println("  Total Credits: " + totalCredits);
            
            double avgCredits = deptCourses.stream()
                .mapToInt(Course::getCredits)
                .average()
                .orElse(0.0);
            System.out.println("  Average Credits per Course: " + String.format("%.1f", avgCredits));
            
            long activeCourses = deptCourses.stream()
                .filter(Course::isActive)
                .count();
            System.out.println("  Active Courses: " + activeCourses);
        });
    }
    
    // Import/Export Methods
    private void handleImportExport() {
        System.out.println("\n--- Import/Export Data ---");
        System.out.println("1. Import Students");
        System.out.println("2. Import Courses");
        System.out.println("3. Import Enrollments");
        System.out.println("4. Export Students");
        System.out.println("5. Export Courses");
        System.out.println("6. Export All Data");
        
        int choice = getIntInput("Enter choice: ");
        
        switch (choice) {
            case 1 -> importStudents();
            case 2 -> importCourses();
            case 3 -> importEnrollments();
            case 4 -> exportStudents();
            case 5 -> exportCourses();
            case 6 -> exportAllData();
            default -> System.out.println("Invalid choice.");
        }
    }
    
    private void importStudents() {
        String filePath = getStringInput("Enter CSV file path (e.g., test-data/students.csv): ");
        
        try {
            List<Student> students = importExportService.importStudents(filePath);
            
            int successCount = 0;
            for (Student student : students) {
                try {
                    studentService.addStudent(student);
                    successCount++;
                } catch (DuplicateEntityException e) {
                    System.out.println("Skipping duplicate student: " + student.getId());
                }
            }
            
            System.out.println("Import completed!");
            System.out.println("Successfully imported: " + successCount + " students");
            System.out.println("Total in file: " + students.size());
            
        } catch (IOException e) {
            System.err.println("Error importing students: " + e.getMessage());
        }
    }
    
    private void importCourses() {
        String filePath = getStringInput("Enter CSV file path (e.g., test-data/courses.csv): ");
        
        try {
            List<Course> courses = importExportService.importCourses(filePath);
            
            int successCount = 0;
            for (Course course : courses) {
                try {
                    courseService.addCourse(course);
                    successCount++;
                } catch (DuplicateEntityException e) {
                    System.out.println("Skipping duplicate course: " + course.getCode());
                }
            }
            
            System.out.println("Import completed!");
            System.out.println("Successfully imported: " + successCount + " courses");
            System.out.println("Total in file: " + courses.size());
            
        } catch (IOException e) {
            System.err.println("Error importing courses: " + e.getMessage());
        }
    }
    
    private void importEnrollments() {
        String filePath = getStringInput("Enter CSV file path (e.g., test-data/enrollments.csv): ");
        
        try {
            List<Enrollment> enrollments = importExportService.importEnrollments(filePath);
            
            int successCount = 0;
            for (Enrollment enrollment : enrollments) {
                try {
                    enrollmentService.enrollStudent(enrollment.getStudentId(), enrollment.getCourseCode());
                    successCount++;
                } catch (DuplicateEnrollmentException | MaxCreditLimitExceededException | EntityNotFoundException e) {
                    System.out.println("Skipping enrollment: " + enrollment.getStudentId() + 
                        " -> " + enrollment.getCourseCode() + " (" + e.getMessage() + ")");
                }
            }
            
            System.out.println("Import completed!");
            System.out.println("Successfully imported: " + successCount + " enrollments");
            System.out.println("Total in file: " + enrollments.size());
            
        } catch (IOException e) {
            System.err.println("Error importing enrollments: " + e.getMessage());
        }
    }
    
    private void exportStudents() {
        String filePath = getStringInput("Enter export file path (e.g., exports/students.csv): ");
        
        try {
            List<Student> students = studentService.getAllStudents();
            importExportService.exportStudents(students, filePath);
            System.out.println("Students exported successfully to: " + filePath);
            System.out.println("Total exported: " + students.size());
            
        } catch (IOException e) {
            System.err.println("Error exporting students: " + e.getMessage());
        }
    }
    
    private void exportCourses() {
        String filePath = getStringInput("Enter export file path (e.g., exports/courses.csv): ");
        
        try {
            List<Course> courses = courseService.getAllCourses();
            importExportService.exportCourses(courses, filePath);
            System.out.println("Courses exported successfully to: " + filePath);
            System.out.println("Total exported: " + courses.size());
            
        } catch (IOException e) {
            System.err.println("Error exporting courses: " + e.getMessage());
        }
    }
    
    private void exportAllData() {
        String exportDir = "exports/full_export_" + 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        
        try {
            importExportService.exportAllData(exportDir);
            System.out.println("All data exported successfully to: " + exportDir);
            
        } catch (IOException e) {
            System.err.println("Error exporting data: " + e.getMessage());
        }
    }
    
    // Backup and Utilities Methods
    private void handleBackupUtilities() {
        System.out.println("\n--- Backup & Utilities ---");
        System.out.println("1. Create Backup");
        System.out.println("2. List Backups");
        System.out.println("3. Calculate Directory Size");
        System.out.println("4. Clean Old Backups");
        System.out.println("5. System Information");
        
        int choice = getIntInput("Enter choice: ");
        
        switch (choice) {
            case 1 -> createBackup();
            case 2 -> listBackups();
            case 3 -> calculateDirectorySize();
            case 4 -> cleanOldBackups();
            case 5 -> showSystemInformation();
            default -> System.out.println("Invalid choice.");
        }
    }
    
    private void createBackup() {
        try {
            System.out.println("Creating backup...");
            String backupPath = backupService.createBackup();
            System.out.println("Backup created successfully!");
            System.out.println("Location: " + backupPath);
            
            // Calculate and display backup size
            long size = backupService.calculateDirectorySize(backupPath);
            System.out.println("Backup size: " + formatFileSize(size));
            
        } catch (IOException e) {
            System.err.println("Error creating backup: " + e.getMessage());
        }
    }
    
    private void listBackups() {
        try {
            List<String> backups = backupService.listBackups();
            
            if (backups.isEmpty()) {
                System.out.println("No backups found.");
                return;
            }
            
            System.out.println("\nAvailable Backups:");
            System.out.println("-".repeat(60));
            
            for (String backup : backups) {
                try {
                    BackupService.BackupInfo info = backupService.getBackupInfo(backup);
                    System.out.printf("%-30s %s (%s)%n", 
                        backup, 
                        info.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        formatFileSize(info.getSize()));
                } catch (IOException e) {
                    System.out.printf("%-30s (Error reading info)%n", backup);
                }
            }
            
            System.out.println("\nTotal backups: " + backups.size());
            
        } catch (IOException e) {
            System.err.println("Error listing backups: " + e.getMessage());
        }
    }
    
    private void calculateDirectorySize() {
        String dirPath = getStringInput("Enter directory path: ");
        
        try {
            long size = backupService.calculateDirectorySize(dirPath);
            System.out.println("Directory size: " + formatFileSize(size));
            
        } catch (IOException e) {
            System.err.println("Error calculating directory size: " + e.getMessage());
        }
    }
    
    private void cleanOldBackups() {
        int keepCount = getIntInput("Enter number of backups to keep: ");
        
        try {
            int deletedCount = backupService.cleanOldBackups(keepCount);
            System.out.println("Cleanup completed!");
            System.out.println("Deleted " + deletedCount + " old backup(s)");
            
        } catch (IOException e) {
            System.err.println("Error cleaning backups: " + e.getMessage());
        }
    }
    
    private void showSystemInformation() {
        System.out.println("\n--- System Information ---");
        System.out.println("Application: " + config.getApplicationName());
        System.out.println("Version: " + config.getVersion());
        System.out.println("Uptime: " + config.getUptime());
        System.out.println("Java Version: " + System.getProperty("java.version"));
        System.out.println("Java Vendor: " + System.getProperty("java.vendor"));
        System.out.println("OS: " + System.getProperty("os.name") + " " + System.getProperty("os.version"));
        System.out.println("Architecture: " + System.getProperty("os.arch"));
        System.out.println("User: " + System.getProperty("user.name"));
        System.out.println("Working Directory: " + System.getProperty("user.dir"));
        
        // Memory information
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        
        System.out.println("\n--- Memory Information ---");
        System.out.println("Max Memory: " + formatFileSize(maxMemory));
        System.out.println("Total Memory: " + formatFileSize(totalMemory));
        System.out.println("Used Memory: " + formatFileSize(usedMemory));
        System.out.println("Free Memory: " + formatFileSize(freeMemory));
    }
    
    // Utility Methods
    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    
    private double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    
    private String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024) return String.format("%.1f MB", bytes / (1024.0 * 1024));
        return String.format("%.1f GB", bytes / (1024.0 * 1024 * 1024));
    }
    
    /**
     * Initialize sample data for demonstration
     * Demonstrates data initialization and error handling
     */
    private void initializeSampleData() {
        System.out.println("Initializing sample data...");
        
        try {
            // Import sample data if files exist
            importSampleDataIfExists();
            
            System.out.println("Sample data initialization completed.");
            System.out.println("Students: " + studentService.getAllStudents().size());
            System.out.println("Courses: " + courseService.getAllCourses().size());
            System.out.println("Enrollments: " + enrollmentService.getAllEnrollments().size());
            System.out.println();
            
        } catch (Exception e) {
            System.out.println("Note: Sample data files not found or could not be loaded.");
            System.out.println("You can import data manually using the Import/Export menu.");
            System.out.println();
        }
    }
    
    private void importSampleDataIfExists() {
        // Try to import sample data files
        String[] sampleFiles = {
            "test-data/students.csv",
            "test-data/courses.csv", 
            "test-data/enrollments.csv"
        };
        
        // Import students
        try {
            List<Student> students = importExportService.importStudents(sampleFiles[0]);
            students.forEach(student -> {
                try {
                    studentService.addStudent(student);
                } catch (DuplicateEntityException e) {
                    // Ignore duplicates during initialization
                }
            });
        } catch (IOException e) {
            // File doesn't exist, skip
        }
        
        // Import courses
        try {
            List<Course> courses = importExportService.importCourses(sampleFiles[1]);
            courses.forEach(course -> {
                try {
                    courseService.addCourse(course);
                } catch (DuplicateEntityException e) {
                    // Ignore duplicates during initialization
                }
            });
        } catch (IOException e) {
            // File doesn't exist, skip
        }
        
        // Import enrollments
        try {
            List<Enrollment> enrollments = importExportService.importEnrollments(sampleFiles[2]);
            enrollments.forEach(enrollment -> {
                try {
                    enrollmentService.enrollStudent(enrollment.getStudentId(), enrollment.getCourseCode());
                } catch (Exception e) {
                    // Ignore errors during initialization
                }
            });
        } catch (IOException e) {
            // File doesn't exist, skip
        }
    }
}