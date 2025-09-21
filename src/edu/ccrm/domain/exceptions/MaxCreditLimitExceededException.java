package edu.ccrm.domain.exceptions;

/**
 * Exception thrown when enrollment would exceed maximum credit limit
 */
public class MaxCreditLimitExceededException extends CCRMException {
    
    private final String studentId;
    private final int currentCredits;
    private final int attemptedCredits;
    private final int maxLimit;
    
    public MaxCreditLimitExceededException(String studentId, int currentCredits, 
                                         int attemptedCredits, int maxLimit) {
        super(String.format("Student '%s' would exceed credit limit: %d + %d > %d", 
              studentId, currentCredits, attemptedCredits, maxLimit),
              "MAX_CREDIT_LIMIT_EXCEEDED");
        this.studentId = studentId;
        this.currentCredits = currentCredits;
        this.attemptedCredits = attemptedCredits;
        this.maxLimit = maxLimit;
    }
    
    public String getStudentId() { return studentId; }
    public int getCurrentCredits() { return currentCredits; }
    public int getAttemptedCredits() { return attemptedCredits; }
    public int getMaxLimit() { return maxLimit; }
}