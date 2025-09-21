package edu.ccrm.domain;

/**
 * Letter grade enum with grade point calculation
 * Demonstrates enum with complex logic
 */
public enum LetterGrade {
    A(4.0, 90, 100, "Excellent"),
    B(3.0, 80, 89, "Good"),
    C(2.0, 70, 79, "Satisfactory"),
    D(1.0, 60, 69, "Poor"),
    F(0.0, 0, 59, "Fail");
    
    private final double gradePoints;
    private final int minMarks;
    private final int maxMarks;
    private final String description;
    
    LetterGrade(double gradePoints, int minMarks, int maxMarks, String description) {
        this.gradePoints = gradePoints;
        this.minMarks = minMarks;
        this.maxMarks = maxMarks;
        this.description = description;
    }
    
    public double getGradePoints() {
        return gradePoints;
    }
    
    public int getMinMarks() {
        return minMarks;
    }
    
    public int getMaxMarks() {
        return maxMarks;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * Get letter grade from marks
     */
    public static LetterGrade fromMarks(double marks) {
        for (LetterGrade grade : values()) {
            if (marks >= grade.minMarks && marks <= grade.maxMarks) {
                return grade;
            }
        }
        return F; // Default to F for invalid marks
    }
    
    /**
     * Check if grade is passing
     */
    public boolean isPassing() {
        return this != F;
    }
    
    /**
     * Check if grade is honor level
     */
    public boolean isHonorLevel() {
        return gradePoints >= 3.5;
    }
    
    /**
     * Get grade category
     */
    public String getCategory() {
        return switch (this) {
            case A -> "Excellent";
            case B -> "Good";
            case C -> "Satisfactory";
            case D -> "Poor";
            case F -> "Failing";
        };
    }
    
    @Override
    public String toString() {
        return name() + " (" + gradePoints + ")";
    }
}