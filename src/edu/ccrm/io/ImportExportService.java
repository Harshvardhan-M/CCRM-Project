package edu.ccrm.io;

import edu.ccrm.domain.*;
import edu.ccrm.domain.exceptions.CCRMException;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for importing and exporting data using NIO.2
 * Demonstrates modern file I/O operations
 */
public class ImportExportService {
    
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Import students from CSV file
     * Demonstrates NIO.2 file reading and Stream API
     */
    public List<Student> importStudents(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        
        if (!Files.exists(path)) {
            throw new IOException("File not found: " + filePath);
        }
        
        List<Student> students = new ArrayList<>();
        
        try (var lines = Files.lines(path)) {
            List<String> csvLines = lines.collect(Collectors.toList());
            
            if (csvLines.isEmpty()) {
                return students;
            }
            
            // Skip header line
            List<String> dataLines = csvLines.subList(1, csvLines.size());
            
            for (String line : dataLines) {
                try {
                    Student student = parseStudentFromCSV(line);
                    if (student != null) {
                        students.add(student);
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing line: " + line + " - " + e.getMessage());
                }
            }
        }
        
        return students;
    }
    
    /**
     * Import courses from CSV file
     */
    public List<Course> importCourses(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        
        if (!Files.exists(path)) {
            throw new IOException("File not found: " + filePath);
        }
        
        List<Course> courses = new ArrayList<>();
        
        try (var lines = Files.lines(path)) {
            List<String> csvLines = lines.collect(Collectors.toList());
            
            if (csvLines.isEmpty()) {
                return courses;
            }
            
            // Skip header line
            List<String> dataLines = csvLines.subList(1, csvLines.size());
            
            for (String line : dataLines) {
                try {
                    Course course = parseCourseFromCSV(line);
                    if (course != null) {
                        courses.add(course);
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing line: " + line + " - " + e.getMessage());
                }
            }
        }
        
        return courses;
    }
    
    /**
     * Import enrollments from CSV file
     */
    public List<Enrollment> importEnrollments(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        
        if (!Files.exists(path)) {
            throw new IOException("File not found: " + filePath);
        }
        
        List<Enrollment> enrollments = new ArrayList<>();
        
        try (var lines = Files.lines(path)) {
            List<String> csvLines = lines.collect(Collectors.toList());
            
            if (csvLines.isEmpty()) {
                return enrollments;
            }
            
            // Skip header line
            List<String> dataLines = csvLines.subList(1, csvLines.size());
            
            for (String line : dataLines) {
                try {
                    Enrollment enrollment = parseEnrollmentFromCSV(line);
                    if (enrollment != null) {
                        enrollments.add(enrollment);
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing line: " + line + " - " + e.getMessage());
                }
            }
        }
        
        return enrollments;
    }
    
    /**
     * Export students to CSV file
     */
    public void exportStudents(List<Student> students, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        
        // Create parent directories if they don't exist
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }
        
        List<String> csvLines = new ArrayList<>();
        
        // Add header
        csvLines.add("ID,RegNo,Name,Email,Status,EnrollmentDate,GPA,TotalCredits");
        
        // Add data
        for (Student student : students) {
            String line = String.format("%s,%s,%s,%s,%s,%s,%.2f,%d",
                student.getId(),
                student.getRegNo(),
                escapeCSV(student.getFullName()),
                student.getEmail(),
                student.getStatus(),
                student.getEnrollmentDate().format(dateFormatter),
                student.getGpa(),
                student.getTotalCredits()
            );
            csvLines.add(line);
        }
        
        Files.write(path, csvLines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
    
    /**
     * Export courses to CSV file
     */
    public void exportCourses(List<Course> courses, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        
        // Create parent directories if they don't exist
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }
        
        List<String> csvLines = new ArrayList<>();
        
        // Add header
        csvLines.add("Code,Title,Credits,Department,Semester,Instructor,Active");
        
        // Add data
        for (Course course : courses) {
            String line = String.format("%s,%s,%d,%s,%s,%s,%s",
                course.getCode(),
                escapeCSV(course.getTitle()),
                course.getCredits(),
                escapeCSV(course.getDepartment()),
                course.getSemester() != null ? course.getSemester().name() : "",
                escapeCSV(course.getInstructor() != null ? course.getInstructor() : ""),
                course.isActive()
            );
            csvLines.add(line);
        }
        
        Files.write(path, csvLines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
    
    /**
     * Export all data to a directory
     */
    public void exportAllData(String exportDir) throws IOException {
        Path dirPath = Paths.get(exportDir);
        Files.createDirectories(dirPath);
        
        // This would export all data types
        // For now, just create placeholder files
        Files.write(dirPath.resolve("students.csv"), 
            Arrays.asList("ID,RegNo,Name,Email,Status,EnrollmentDate,GPA,TotalCredits"));
        Files.write(dirPath.resolve("courses.csv"), 
            Arrays.asList("Code,Title,Credits,Department,Semester,Instructor,Active"));
        Files.write(dirPath.resolve("enrollments.csv"), 
            Arrays.asList("StudentID,CourseCode,EnrollmentDate,Status"));
        Files.write(dirPath.resolve("grades.csv"), 
            Arrays.asList("StudentID,CourseCode,Marks,LetterGrade,GradePoints,RecordedDate"));
    }
    
    // Private helper methods
    private Student parseStudentFromCSV(String line) {
        String[] parts = line.split(",");
        
        if (parts.length < 4) {
            throw new IllegalArgumentException("Invalid student CSV format");
        }
        
        try {
            return new Student.Builder()
                .setId(parts[0].trim())
                .setRegNo(parts[1].trim())
                .setFullName(parts[2].trim())
                .setEmail(parts[3].trim())
                .setStatus(parts.length > 4 ? StudentStatus.valueOf(parts[4].trim().toUpperCase()) : StudentStatus.ACTIVE)
                .setEnrollmentDate(LocalDateTime.now())
                .build();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error parsing student: " + e.getMessage());
        }
    }
    
    private Course parseCourseFromCSV(String line) {
        String[] parts = line.split(",");
        
        if (parts.length < 4) {
            throw new IllegalArgumentException("Invalid course CSV format");
        }
        
        try {
            Course.Builder builder = new Course.Builder(parts[0].trim(), parts[1].trim())
                .credits(Integer.parseInt(parts[2].trim()))
                .department(parts[3].trim());
            
            if (parts.length > 4 && !parts[4].trim().isEmpty()) {
                builder.semester(Semester.valueOf(parts[4].trim().toUpperCase()));
            }
            
            if (parts.length > 5 && !parts[5].trim().isEmpty()) {
                builder.instructor(parts[5].trim());
            }
            
            return builder.build();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error parsing course: " + e.getMessage());
        }
    }
    
    private Enrollment parseEnrollmentFromCSV(String line) {
        String[] parts = line.split(",");
        
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid enrollment CSV format");
        }
        
        try {
            Enrollment enrollment = new Enrollment(parts[0].trim(), parts[1].trim());
            
            if (parts.length > 2 && !parts[2].trim().isEmpty()) {
                // Parse enrollment date if provided
                LocalDateTime enrollmentDate = LocalDateTime.parse(parts[2].trim(), dateFormatter);
                enrollment.setEnrollmentDate(enrollmentDate);
            }
            
            if (parts.length > 3 && !parts[3].trim().isEmpty()) {
                enrollment.setStatus(Enrollment.EnrollmentStatus.valueOf(parts[3].trim().toUpperCase()));
            }
            
            return enrollment;
        } catch (Exception e) {
            throw new IllegalArgumentException("Error parsing enrollment: " + e.getMessage());
        }
    }
    
    private String escapeCSV(String value) {
        if (value == null) return "";
        
        // Escape commas and quotes in CSV values
        if (value.contains(",") || value.contains("\"")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        
        return value;
    }
}