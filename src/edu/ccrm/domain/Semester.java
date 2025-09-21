package edu.ccrm.domain;

/**
 * Semester enum with constructor, fields, and methods
 */
public enum Semester {
    SPRING("Spring", "January - May", 1),
    SUMMER("Summer", "June - August", 2),
    FALL("Fall", "September - December", 3);
    
    private final String displayName;
    private final String period;
    private final int order;
    
    Semester(String displayName, String period, int order) {
        this.displayName = displayName;
        this.period = period;
        this.order = order;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getPeriod() {
        return period;
    }
    
    public int getOrder() {
        return order;
    }
    
    /**
     * Get next semester in sequence
     */
    public Semester next() {
        return switch (this) {
            case SPRING -> SUMMER;
            case SUMMER -> FALL;
            case FALL -> SPRING;
        };
    }
    
    /**
     * Get previous semester in sequence
     */
    public Semester previous() {
        return switch (this) {
            case SPRING -> FALL;
            case SUMMER -> SPRING;
            case FALL -> SUMMER;
        };
    }
    
    /**
     * Check if this is a regular academic semester (not summer)
     */
    public boolean isRegularSemester() {
        return this != SUMMER;
    }
    
    /**
     * Get semester from display name
     */
    public static Semester fromDisplayName(String displayName) {
        for (Semester semester : values()) {
            if (semester.displayName.equalsIgnoreCase(displayName)) {
                return semester;
            }
        }
        throw new IllegalArgumentException("Unknown semester: " + displayName);
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}