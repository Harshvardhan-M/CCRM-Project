package edu.ccrm.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Student class extending Person - demonstrates inheritance and Builder pattern
 */
public class Student extends Person {
    
    private String regNo;
    private StudentStatus status;
    private LocalDateTime enrollmentDate;
    private Set<String> enrolledCourses;
    private double gpa;
    private int totalCredits;
    
    // Private constructor for Builder pattern
    private Student(Builder builder) {
        super(builder.id, builder.fullName, builder.email);
        this.regNo = builder.regNo;
        this.status = builder.status;
        this.enrollmentDate = builder.enrollmentDate;
        this.enrolledCourses = new HashSet<>(builder.enrolledCourses);
        this.gpa = builder.gpa;
        this.totalCredits = builder.totalCredits;
    }
    
    // Implementation of abstract methods from Person
    @Override
    public String getDisplayRole() {
        return "Student";
    }
    
    @Override
    public boolean isEligibleForEnrollment() {
        return status == StudentStatus.ACTIVE;
    }
    
    // Getters and setters
    public String getRegNo() {
        return regNo;
    }
    
    public void setRegNo(String regNo) {
        this.regNo = regNo;
        updateLastModified();
    }
    
    public StudentStatus getStatus() {
        return status;
    }
    
    public void setStatus(StudentStatus status) {
        this.status = status;
        updateLastModified();
    }
    
    public LocalDateTime getEnrollmentDate() {
        return enrollmentDate;
    }
    
    public void setEnrollmentDate(LocalDateTime enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
        updateLastModified();
    }
    
    public Set<String> getEnrolledCourses() {
        return Collections.unmodifiableSet(enrolledCourses);
    }
    
    public void addEnrolledCourse(String courseCode) {
        enrolledCourses.add(courseCode);
        updateLastModified();
    }
    
    public void removeEnrolledCourse(String courseCode) {
        enrolledCourses.remove(courseCode);
        updateLastModified();
    }
    
    public double getGpa() {
        return gpa;
    }
    
    public void setGpa(double gpa) {
        assert gpa >= 0.0 && gpa <= 4.0 : "GPA must be between 0.0 and 4.0: " + gpa;
        this.gpa = gpa;
        updateLastModified();
    }
    
    public int getTotalCredits() {
        return totalCredits;
    }
    
    public void setTotalCredits(int totalCredits) {
        assert totalCredits >= 0 : "Total credits cannot be negative: " + totalCredits;
        this.totalCredits = totalCredits;
        updateLastModified();
    }
    
    // Override toString for proper display
    @Override
    public String toString() {
        return String.format("""
            Student Information:
            - ID: %s
            - Registration No: %s
            - Name: %s
            - Email: %s
            - Status: %s
            - Enrollment Date: %s
            - GPA: %.2f
            - Total Credits: %d
            - Enrolled Courses: %d
            """, 
            id, regNo, fullName, email, status,
            enrollmentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
            gpa, totalCredits, enrolledCourses.size());
    }
    
    /**
     * Static nested Builder class demonstrating Builder design pattern
     */
    public static class Builder {
        private String id;
        private String regNo;
        private String fullName;
        private String email;
        private StudentStatus status = StudentStatus.ACTIVE;
        private LocalDateTime enrollmentDate = LocalDateTime.now();
        private Set<String> enrolledCourses = new HashSet<>();
        private double gpa = 0.0;
        private int totalCredits = 0;
        
        public Builder setId(String id) {
            this.id = id;
            return this;
        }
        
        public Builder setRegNo(String regNo) {
            this.regNo = regNo;
            return this;
        }
        
        public Builder setFullName(String fullName) {
            this.fullName = fullName;
            return this;
        }
        
        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }
        
        public Builder setStatus(StudentStatus status) {
            this.status = status;
            return this;
        }
        
        public Builder setEnrollmentDate(LocalDateTime enrollmentDate) {
            this.enrollmentDate = enrollmentDate;
            return this;
        }
        
        public Builder addEnrolledCourse(String courseCode) {
            this.enrolledCourses.add(courseCode);
            return this;
        }
        
        public Builder setEnrolledCourses(Set<String> enrolledCourses) {
            this.enrolledCourses = new HashSet<>(enrolledCourses);
            return this;
        }
        
        public Builder setGpa(double gpa) {
            this.gpa = gpa;
            return this;
        }
        
        public Builder setTotalCredits(int totalCredits) {
            this.totalCredits = totalCredits;
            return this;
        }
        
        public Student build() {
            // Validation before building
            Objects.requireNonNull(id, "Student ID is required");
            Objects.requireNonNull(regNo, "Registration number is required");
            Objects.requireNonNull(fullName, "Full name is required");
            Objects.requireNonNull(email, "Email is required");
            
            if (!email.contains("@")) {
                throw new IllegalArgumentException("Invalid email format");
            }
            
            return new Student(this);
        }
    }
    
    /**
     * Immutable value class for student profile summary
     */
    public static final class ProfileSummary {
        private final String studentId;
        private final String name;
        private final double gpa;
        private final int totalCredits;
        private final int enrolledCourses;
        private final LocalDateTime lastActivity;
        
        public ProfileSummary(Student student) {
            this.studentId = student.getId();
            this.name = student.getFullName();
            this.gpa = student.getGpa();
            this.totalCredits = student.getTotalCredits();
            this.enrolledCourses = student.getEnrolledCourses().size();
            this.lastActivity = student.getLastModified();
        }
        
        // Getters only (immutable)
        public String getStudentId() { return studentId; }
        public String getName() { return name; }
        public double getGpa() { return gpa; }
        public int getTotalCredits() { return totalCredits; }
        public int getEnrolledCourses() { return enrolledCourses; }
        public LocalDateTime getLastActivity() { return lastActivity; }
        
        @Override
        public String toString() {
            return String.format("ProfileSummary{id='%s', name='%s', gpa=%.2f, credits=%d}", 
                studentId, name, gpa, totalCredits);
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            
            ProfileSummary that = (ProfileSummary) obj;
            return Objects.equals(studentId, that.studentId);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(studentId);
        }
    }
}