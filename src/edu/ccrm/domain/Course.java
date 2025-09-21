package edu.ccrm.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Course class with Builder pattern demonstration
 */
public class Course {
    
    private String code;
    private String title;
    private int credits;
    private String department;
    private Semester semester;
    private String instructor;
    private boolean isActive;
    private LocalDateTime createdDate;
    private LocalDateTime lastModified;
    private String description;
    private String prerequisites;
    
    // Private constructor for Builder
    private Course(Builder builder) {
        this.code = builder.code;
        this.title = builder.title;
        this.credits = builder.credits;
        this.department = builder.department;
        this.semester = builder.semester;
        this.instructor = builder.instructor;
        this.isActive = builder.isActive;
        this.description = builder.description;
        this.prerequisites = builder.prerequisites;
        this.createdDate = LocalDateTime.now();
        this.lastModified = LocalDateTime.now();
        
        // Validation
        validateCourse();
    }
    
    private void validateCourse() {
        assert code != null && !code.trim().isEmpty() : "Course code cannot be empty";
        assert title != null && !title.trim().isEmpty() : "Course title cannot be empty";
        assert credits > 0 && credits <= 6 : "Credits must be between 1-6: " + credits;
        assert department != null && !department.trim().isEmpty() : "Department cannot be empty";
    }
    
    // Getters
    public String getCode() { return code; }
    public String getTitle() { return title; }
    public int getCredits() { return credits; }
    public String getDepartment() { return department; }
    public Semester getSemester() { return semester; }
    public String getInstructor() { return instructor; }
    public boolean isActive() { return isActive; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public LocalDateTime getLastModified() { return lastModified; }
    public String getDescription() { return description; }
    public String getPrerequisites() { return prerequisites; }
    
    // Setters
    public void setTitle(String title) {
        assert title != null && !title.trim().isEmpty() : "Title cannot be empty";
        this.title = title;
        this.lastModified = LocalDateTime.now();
    }
    
    public void setCredits(int credits) {
        assert credits > 0 && credits <= 6 : "Credits must be between 1-6";
        this.credits = credits;
        this.lastModified = LocalDateTime.now();
    }
    
    public void setDepartment(String department) {
        this.department = department;
        this.lastModified = LocalDateTime.now();
    }
    
    public void setSemester(Semester semester) {
        this.semester = semester;
        this.lastModified = LocalDateTime.now();
    }
    
    public void setInstructor(String instructor) {
        this.instructor = instructor;
        this.lastModified = LocalDateTime.now();
    }
    
    public void setActive(boolean active) {
        this.isActive = active;
        this.lastModified = LocalDateTime.now();
    }
    
    public void setDescription(String description) {
        this.description = description;
        this.lastModified = LocalDateTime.now();
    }
    
    public void setPrerequisites(String prerequisites) {
        this.prerequisites = prerequisites;
        this.lastModified = LocalDateTime.now();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Course course = (Course) obj;
        return Objects.equals(code, course.code);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
    
    @Override
    public String toString() {
        return String.format("""
            Course Information:
            - Code: %s
            - Title: %s
            - Credits: %d
            - Department: %s
            - Semester: %s
            - Instructor: %s
            - Status: %s
            - Created: %s
            """,
            code, title, credits, department,
            semester != null ? semester.getDisplayName() : "Not assigned",
            instructor != null ? instructor : "Not assigned",
            isActive ? "Active" : "Inactive",
            createdDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    }
    
    /**
     * Static Builder class demonstrating Builder design pattern
     */
    public static class Builder {
        private String code;
        private String title;
        private int credits = 3; // default
        private String department;
        private Semester semester;
        private String instructor;
        private boolean isActive = true;
        private String description;
        private String prerequisites;
        
        public Builder(String code, String title) {
            this.code = code;
            this.title = title;
        }
        
        public Builder credits(int credits) {
            this.credits = credits;
            return this;
        }
        
        public Builder department(String department) {
            this.department = department;
            return this;
        }
        
        public Builder semester(Semester semester) {
            this.semester = semester;
            return this;
        }
        
        public Builder instructor(String instructor) {
            this.instructor = instructor;
            return this;
        }
        
        public Builder active(boolean isActive) {
            this.isActive = isActive;
            return this;
        }
        
        public Builder description(String description) {
            this.description = description;
            return this;
        }
        
        public Builder prerequisites(String prerequisites) {
            this.prerequisites = prerequisites;
            return this;
        }
        
        public Course build() {
            Objects.requireNonNull(code, "Course code is required");
            Objects.requireNonNull(title, "Course title is required");
            Objects.requireNonNull(department, "Department is required");
            
            return new Course(this);
        }
    }
    
    /**
     * Immutable value class for course code
     * Demonstrates immutability and defensive copying
     */
    public static final class CourseCode {
        private final String code;
        private final String subject;
        private final int number;
        
        public CourseCode(String code) {
            if (code == null || !code.matches("^[A-Z]{2,4}\\d{3}$")) {
                throw new IllegalArgumentException("Invalid course code format: " + code);
            }
            
            this.code = code;
            this.subject = code.replaceAll("\\d", "");
            this.number = Integer.parseInt(code.replaceAll("\\D", ""));
        }
        
        public String getCode() { return code; }
        public String getSubject() { return subject; }
        public int getNumber() { return number; }
        
        public int getLevel() {
            return number / 100; // 1 for 100-level, 2 for 200-level, etc.
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            
            CourseCode that = (CourseCode) obj;
            return Objects.equals(code, that.code);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(code);
        }
        
        @Override
        public String toString() {
            return code;
        }
    }
}