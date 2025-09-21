package edu.ccrm.domain.exceptions;

/**
 * Exception thrown when student is already enrolled in a course
 */
public class DuplicateEnrollmentException extends CCRMException {
    
    private final String studentId;
    private final String courseCode;
    
    public DuplicateEnrollmentException(String studentId, String courseCode) {
        super(String.format("Student '%s' is already enrolled in course '%s'", studentId, courseCode),
              "DUPLICATE_ENROLLMENT");
        this.studentId = studentId;
        this.courseCode = courseCode;
    }
    
    public String getStudentId() {
        return studentId;
    }
    
    public String getCourseCode() {
        return courseCode;
    }
}