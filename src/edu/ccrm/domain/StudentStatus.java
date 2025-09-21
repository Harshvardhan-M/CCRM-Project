package edu.ccrm.domain;

/**
 * Enum for student status with constructor and fields
 * Demonstrates enum with methods and fields
 */
public enum StudentStatus {
    ACTIVE("Active", "Student is currently enrolled and active"),
    INACTIVE("Inactive", "Student is temporarily inactive"),
    GRADUATED("Graduated", "Student has completed their program"),
    SUSPENDED("Suspended", "Student is currently suspended"),
    WITHDRAWN("Withdrawn", "Student has withdrawn from the program");
    
    private final String displayName;
    private final String description;
    
    // Enum constructor
    StudentStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * Check if status allows enrollment
     */
    public boolean allowsEnrollment() {
        return this == ACTIVE;
    }
    
    /**
     * Check if status allows grade changes
     */
    public boolean allowsGradeChanges() {
        return this == ACTIVE || this == INACTIVE;
    }
    
    /**
     * Get status by display name
     */
    public static StudentStatus fromDisplayName(String displayName) {
        for (StudentStatus status : values()) {
            if (status.displayName.equalsIgnoreCase(displayName)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + displayName);
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}