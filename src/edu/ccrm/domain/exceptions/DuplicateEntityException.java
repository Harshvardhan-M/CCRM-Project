package edu.ccrm.domain.exceptions;

/**
 * Exception thrown when attempting to create duplicate entities
 * Demonstrates custom checked exception
 */
public class DuplicateEntityException extends CCRMException {
    
    private final String entityType;
    private final String entityId;
    
    public DuplicateEntityException(String entityType, String entityId) {
        super(String.format("%s with ID '%s' already exists", entityType, entityId), 
              "DUPLICATE_ENTITY");
        this.entityType = entityType;
        this.entityId = entityId;
    }
    
    public DuplicateEntityException(String message, String entityType, String entityId) {
        super(message, "DUPLICATE_ENTITY");
        this.entityType = entityType;
        this.entityId = entityId;
    }
    
    public String getEntityType() {
        return entityType;
    }
    
    public String getEntityId() {
        return entityId;
    }
}