package edu.ccrm.domain;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Abstract base class demonstrating inheritance and abstraction
 * Serves as parent for Student and Instructor classes
 */
public abstract class Person {
    
    protected String id;
    protected String fullName;
    protected String email;
    protected LocalDateTime createdDate;
    protected LocalDateTime lastModified;
    
    /**
     * Protected constructor for inheritance
     */
    protected Person() {
        this.createdDate = LocalDateTime.now();
        this.lastModified = LocalDateTime.now();
    }
    
    /**
     * Constructor with basic information
     */
    protected Person(String id, String fullName, String email) {
        this();
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        
        // Assertions for validation
        assert id != null && !id.trim().isEmpty() : "ID cannot be null or empty";
        assert email != null && email.contains("@") : "Email must be valid";
    }
    
    // Abstract methods to be implemented by subclasses
    public abstract String getDisplayRole();
    public abstract boolean isEligibleForEnrollment();
    
    // Getters and setters with encapsulation
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        assert id != null && !id.trim().isEmpty() : "ID cannot be null or empty";
        this.id = id;
        updateLastModified();
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        assert fullName != null && !fullName.trim().isEmpty() : "Name cannot be null or empty";
        this.fullName = fullName;
        updateLastModified();
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        assert email != null && email.contains("@") : "Email must be valid: " + email;
        this.email = email;
        updateLastModified();
    }
    
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    
    public LocalDateTime getLastModified() {
        return lastModified;
    }
    
    /**
     * Update last modified timestamp
     */
    protected void updateLastModified() {
        this.lastModified = LocalDateTime.now();
    }
    
    // Override equals and hashCode for proper object comparison
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Person person = (Person) obj;
        return Objects.equals(id, person.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    // Override toString for polymorphic display
    @Override
    public String toString() {
        return String.format("%s: %s (ID: %s, Email: %s)", 
            getDisplayRole(), fullName, id, email);
    }
    
    /**
     * Static nested class for name validation
     */
    public static class NameValidator {
        public static boolean isValidName(String name) {
            return name != null && 
                   name.trim().length() >= 2 && 
                   name.matches("^[a-zA-Z\\s\\-\\.]+$");
        }
        
        public static String formatName(String name) {
            if (!isValidName(name)) {
                throw new IllegalArgumentException("Invalid name format");
            }
            
            return name.trim()
                      .replaceAll("\\s+", " ")
                      .toLowerCase()
                      .replaceAll("\\b\\w", match -> match.toUpperCase());
        }
    }
    
    /**
     * Inner class for contact information (demonstrating inner class)
     */
    public class ContactInfo {
        private String primaryEmail;
        private String secondaryEmail;
        private String phoneNumber;
        
        public ContactInfo(String primaryEmail) {
            this.primaryEmail = primaryEmail;
            
            // Access to outer class members
            Person.this.email = primaryEmail;
        }
        
        public String getPrimaryEmail() { return primaryEmail; }
        public void setPrimaryEmail(String email) {
            this.primaryEmail = email;
            Person.this.setEmail(email); // Update outer class
        }
        
        public String getSecondaryEmail() { return secondaryEmail; }
        public void setSecondaryEmail(String email) { this.secondaryEmail = email; }
        
        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String phone) { this.phoneNumber = phone; }
        
        @Override
        public String toString() {
            return String.format("ContactInfo{primary='%s', secondary='%s', phone='%s'}", 
                primaryEmail, secondaryEmail, phoneNumber);
        }
    }
}