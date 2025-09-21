package edu.ccrm.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Grade entity for recording student performance
 */
public class Grade {
    
    private String studentId;
    private String courseCode;
    private double marks;
    private LetterGrade letterGrade;
    private double gradePoints;
    private LocalDateTime recordedDate;
    private LocalDateTime lastModified;
    private String comments;
    
    public Grade() {
        this.recordedDate = LocalDateTime.now();
        this.lastModified = LocalDateTime.now();
    }
    
    public Grade(String studentId, String courseCode, double marks) {
        this();
        this.studentId = studentId;
        this.courseCode = courseCode;
        setMarks(marks); // This will calculate letter grade and points
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
    
    public double getMarks() {
        return marks;
    }
    
    public void setMarks(double marks) {
        if (marks < 0 || marks > 100) {
            throw new IllegalArgumentException("Marks must be between 0 and 100: " + marks);
        }
        
        this.marks = marks;
        this.letterGrade = LetterGrade.fromMarks(marks);
        this.gradePoints = letterGrade.getGradePoints();
        this.lastModified = LocalDateTime.now();
    }
    
    public LetterGrade getLetterGrade() {
        return letterGrade;
    }
    
    public double getGradePoints() {
        return gradePoints;
    }
    
    public LocalDateTime getRecordedDate() {
        return recordedDate;
    }
    
    public void setRecordedDate(LocalDateTime recordedDate) {
        this.recordedDate = recordedDate;
        this.lastModified = LocalDateTime.now();
    }
    
    public LocalDateTime getLastModified() {
        return lastModified;
    }
    
    public String getComments() {
        return comments;
    }
    
    public void setComments(String comments) {
        this.comments = comments;
        this.lastModified = LocalDateTime.now();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Grade grade = (Grade) obj;
        return Objects.equals(studentId, grade.studentId) &&
               Objects.equals(courseCode, grade.courseCode);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(studentId, courseCode);
    }
    
    @Override
    public String toString() {
        return String.format("""
            Grade Record:
            - Student ID: %s
            - Course Code: %s
            - Marks: %.1f
            - Letter Grade: %s
            - Grade Points: %.2f
            - Recorded: %s
            """,
            studentId, courseCode, marks, letterGrade, gradePoints,
            recordedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    }
}