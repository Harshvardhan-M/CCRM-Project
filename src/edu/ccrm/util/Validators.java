package edu.ccrm.util;

import edu.ccrm.domain.Student;
import edu.ccrm.domain.Course;

import java.util.regex.Pattern;

/**
 * Utility class for validation operations
 * Demonstrates utility class design and validation patterns
 */
public final class Validators {
    
    // Email validation pattern
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    // Student ID pattern (3 letters + 3 digits)
    private static final Pattern STUDENT_ID_PATTERN = 
        Pattern.compile("^STU\\d{3}$");
    
    // Course code pattern (2-4 letters + 3 digits)
    private static final Pattern COURSE_CODE_PATTERN = 
        Pattern.compile("^[A-Z]{2,4}\\d{3}$");
    
    // Registration number pattern
    private static final Pattern REG_NO_PATTERN = 
        Pattern.compile("^REG\\d{7}$");
    
    private Validators() {
        throw new AssertionError("Utility class should not be instantiated");
    }
    
    /**
     * Validate email format
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * Validate student ID format
     */
    public static boolean isValidStudentId(String studentId) {
        return studentId != null && STUDENT_ID_PATTERN.matcher(studentId).matches();
    }
    
    /**
     * Validate course code format
     */
    public static boolean isValidCourseCode(String courseCode) {
        return courseCode != null && COURSE_CODE_PATTERN.matcher(courseCode).matches();
    }
    
    /**
     * Validate registration number format
     */
    public static boolean isValidRegNo(String regNo) {
        return regNo != null && REG_NO_PATTERN.matcher(regNo).matches();
    }
    
    /**
     * Validate name (contains only letters, spaces, hyphens, and periods)
     */
    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        
        return name.trim().matches("^[a-zA-Z\\s\\-\\.]+$") && name.trim().length() >= 2;
    }
    
    /**
     * Validate GPA range
     */
    public static boolean isValidGPA(double gpa) {
        return gpa >= 0.0 && gpa <= 4.0;
    }
    
    /**
     * Validate marks range
     */
    public static boolean isValidMarks(double marks) {
        return marks >= 0.0 && marks <= 100.0;
    }
    
    /**
     * Validate course credits
     */
    public static boolean isValidCredits(int credits) {
        return credits >= 1 && credits <= 6;
    }
    
    /**
     * Validate student completeness
     */
    public static ValidationResult validateStudent(Student student) {
        if (student == null) {
            return new ValidationResult(false, "Student cannot be null");
        }
        
        if (!isValidStudentId(student.getId())) {
            return new ValidationResult(false, "Invalid student ID format");
        }
        
        if (!isValidRegNo(student.getRegNo())) {
            return new ValidationResult(false, "Invalid registration number format");
        }
        
        if (!isValidName(student.getFullName())) {
            return new ValidationResult(false, "Invalid student name");
        }
        
        if (!isValidEmail(student.getEmail())) {
            return new ValidationResult(false, "Invalid email format");
        }
        
        if (!isValidGPA(student.getGpa())) {
            return new ValidationResult(false, "GPA must be between 0.0 and 4.0");
        }
        
        return new ValidationResult(true, "Valid student");
    }
    
    /**
     * Validate course completeness
     */
    public static ValidationResult validateCourse(Course course) {
        if (course == null) {
            return new ValidationResult(false, "Course cannot be null");
        }
        
        if (!isValidCourseCode(course.getCode())) {
            return new ValidationResult(false, "Invalid course code format");
        }
        
        if (course.getTitle() == null || course.getTitle().trim().isEmpty()) {
            return new ValidationResult(false, "Course title is required");
        }
        
        if (!isValidCredits(course.getCredits())) {
            return new ValidationResult(false, "Course credits must be between 1 and 6");
        }
        
        if (course.getDepartment() == null || course.getDepartment().trim().isEmpty()) {
            return new ValidationResult(false, "Department is required");
        }
        
        return new ValidationResult(true, "Valid course");
    }
    
    /**
     * Validate string is not null or empty
     */
    public static boolean isNotNullOrEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }
    
    /**
     * Validate string length is within range
     */
    public static boolean isLengthInRange(String str, int minLength, int maxLength) {
        if (str == null) {
            return false;
        }
        
        int length = str.trim().length();
        return length >= minLength && length <= maxLength;
    }
    
    /**
     * Validate numeric range
     */
    public static boolean isInRange(double value, double min, double max) {
        return value >= min && value <= max;
    }
    
    /**
     * Validation result class
     */
    public static class ValidationResult {
        private final boolean valid;
        private final String message;
        
        public ValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }
        
        public boolean isValid() {
            return valid;
        }
        
        public String getMessage() {
            return message;
        }
        
        @Override
        public String toString() {
            return String.format("ValidationResult{valid=%s, message='%s'}", valid, message);
        }
    }
}