package edu.ccrm.domain.exceptions;

/**
 * Exception thrown when an entity is not found
 * Demonstrates custom checked exception
 */
public class EntityNotFoundException extends CCRMException {
    
    private final String entityType;
    private final String entityId;
    
    public EntityNotFoundException(String entityType, String entityId) {
        super(String.format("%s with ID '%s' not found", entityType, entityId), 
              "ENTITY_NOT_FOUND");
        this.entityType = entityType;
        this.entityId = entityId;
    }
    
    public EntityNotFoundException(String message, String entityType, String entityId) {
        super(message, "ENTITY_NOT_FOUND");
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