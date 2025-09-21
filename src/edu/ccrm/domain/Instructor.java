package edu.ccrm.domain;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Instructor class extending Person - demonstrates inheritance
 */
public class Instructor extends Person {
    
    private String employeeId;
    private String department;
    private String title;
    private Set<String> assignedCourses;
    private LocalDateTime hireDate;
    
    public Instructor() {
        super();
        this.assignedCourses = new HashSet<>();
    }
    
    public Instructor(String id, String fullName, String email, String employeeId, String department) {
        super(id, fullName, email);
        this.employeeId = employeeId;
        this.department = department;
        this.assignedCourses = new HashSet<>();
        this.hireDate = LocalDateTime.now();
    }
    
    // Implementation of abstract methods
    @Override
    public String getDisplayRole() {
        return "Instructor";
    }
    
    @Override
    public boolean isEligibleForEnrollment() {
        return false; // Instructors cannot enroll in courses
    }
    
    // Getters and setters
    public String getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
        updateLastModified();
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
        updateLastModified();
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
        updateLastModified();
    }
    
    public Set<String> getAssignedCourses() {
        return Collections.unmodifiableSet(assignedCourses);
    }
    
    public void assignCourse(String courseCode) {
        assignedCourses.add(courseCode);
        updateLastModified();
    }
    
    public void unassignCourse(String courseCode) {
        assignedCourses.remove(courseCode);
        updateLastModified();
    }
    
    public LocalDateTime getHireDate() {
        return hireDate;
    }
    
    public void setHireDate(LocalDateTime hireDate) {
        this.hireDate = hireDate;
        updateLastModified();
    }
    
    @Override
    public String toString() {
        return String.format("""
            Instructor Information:
            - ID: %s
            - Employee ID: %s
            - Name: %s
            - Email: %s
            - Department: %s
            - Title: %s
            - Assigned Courses: %d
            - Hire Date: %s
            """,
            id, employeeId, fullName, email, department, 
            title != null ? title : "Not specified",
            assignedCourses.size(),
            hireDate != null ? hireDate.toLocalDate().toString() : "Not specified");
    }
}