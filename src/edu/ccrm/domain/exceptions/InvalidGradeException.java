package edu.ccrm.domain.exceptions;

/**
 * Exception thrown for invalid grade operations
 * Demonstrates custom unchecked exception
 */
public class InvalidGradeException extends RuntimeException {
    
    private final String studentId;
    private final String courseCode;
    private final Object invalidValue;
    
    public InvalidGradeException(String message) {
        super(message);
        this.studentId = null;
        this.courseCode = null;
        this.invalidValue = null;
    }
    
    public InvalidGradeException(String message, String studentId, String courseCode, Object invalidValue) {
        super(message);
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.invalidValue = invalidValue;
    }
    
    public String getStudentId() { return studentId; }
    public String getCourseCode() { return courseCode; }
    public Object getInvalidValue() { return invalidValue; }
}