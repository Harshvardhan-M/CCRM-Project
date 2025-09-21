package edu.ccrm.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Enrollment entity representing student-course relationship
 */
public class Enrollment {
    
    private String studentId;
    private String courseCode;
    private LocalDateTime enrollmentDate;
    private EnrollmentStatus status;
    private LocalDateTime lastModified;
    
    public Enrollment() {
        this.enrollmentDate = LocalDateTime.now();
        this.lastModified = LocalDateTime.now();
        this.status = EnrollmentStatus.ENROLLED;
    }
    
    public Enrollment(String studentId, String courseCode) {
        this();
        this.studentId = studentId;
        this.courseCode = courseCode;
        
        assert studentId != null && !studentId.trim().isEmpty() : "Student ID cannot be empty";
        assert courseCode != null && !courseCode.trim().isEmpty() : "Course code cannot be empty";
    }
    
    // Getters and setters
    public String getStudentId() {
        return studentId;
    }
    
    public void setStudentId(String studentId) {
        this.studentId = studentId;
        this.lastModified = LocalDateTime.now();
    }
    
    public String getCourseCode() {
        return courseCode;
    }
    
    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
        this.lastModified = LocalDateTime.now();
    }
    
    public LocalDateTime getEnrollmentDate() {
        return enrollmentDate;
    }
    
    public void setEnrollmentDate(LocalDateTime enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
        this.lastModified = LocalDateTime.now();
    }
    
    public EnrollmentStatus getStatus() {
        return status;
    }
    
    public void setStatus(EnrollmentStatus status) {
        this.status = status;
        this.lastModified = LocalDateTime.now();
    }
    
    public LocalDateTime getLastModified() {
        return lastModified;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Enrollment that = (Enrollment) obj;
        return Objects.equals(studentId, that.studentId) &&
               Objects.equals(courseCode, that.courseCode);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(studentId, courseCode);
    }
    
    @Override
    public String toString() {
        return String.format("Enrollment{student='%s', course='%s', date='%s', status='%s'}", 
            studentId, courseCode, 
            enrollmentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
            status);
    }
    
    /**
     * Enrollment status enum
     */
    public enum EnrollmentStatus {
        ENROLLED("Enrolled", "Student is currently enrolled"),
        DROPPED("Dropped", "Student has dropped the course"),
        COMPLETED("Completed", "Course has been completed"),
        WITHDRAWN("Withdrawn", "Student withdrew from course");
        
        private final String displayName;
        private final String description;
        
        EnrollmentStatus(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        
        @Override
        public String toString() { return displayName; }
    }
}