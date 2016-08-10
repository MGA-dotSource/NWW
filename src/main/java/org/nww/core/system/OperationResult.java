/**
 *
 */
package org.nww.core.system;

import org.nww.core.data.PersistentObject;

/**
 * This class is intended to hold read only information about the result of a
 * persistent object operation.
 *
 * @author MGA
 *
 */
public class OperationResult {

    /**
     * The operation result state basically defines whether the operation
     * succeeded or not.
     *
     * @author MGA
     *
     */
    public enum State {
        SUCCESSFULL, PARTLY_SUCCESSFULL, FAILED, WARNING, NONE, NOT_ALLOWED
    }

    /**
     * Create a new operation result object.
     *
     * @param resultState the result state
     */
    public OperationResult(State resultState) {
        this(resultState, 0l, "", "", null);
    }

    /**
     * Create a new operation result object.
     *
     * @param resultState the result state
     * @param exception a possible thrown exception
     */
    public OperationResult(State resultState, Exception exception) {
        this(resultState, 0l, "", exception);
    }

    /**
     * Create a new operation result object.
     *
     * @param resultState the result state
     * @param affectedEntitiesCount the number of affected entities
     */
    public OperationResult(State resultState, Long affectedEntitiesCount) {
        this(resultState, affectedEntitiesCount, "", "", null);
    }

    /**
     * Create a new operation result object.
     *
     * @param resultState the result state
     * @param affectedEntitiesCount the number of affected entities
     * @param exception a possible thrown exception
     */
    public OperationResult(State resultState, Long affectedEntitiesCount, Exception exception) {
        this(resultState, affectedEntitiesCount, "", "", null, exception);
    }

    /**
     * Create a new operation result object.
     *
     * @param resultState the result state
     * @param affectedEntitiesCount the number of affected entities
     * @param message an additional description message (possible error message)
     */
    public OperationResult(State resultState, Long affectedEntitiesCount,
            String message) {
        this(resultState, affectedEntitiesCount, message, "", null);
    }

    /**
     * Create a new operation result object.
     *
     * @param resultState the result state
     * @param affectedEntitiesCount the number of affected entities
     * @param message an additional description message (possible error message)
     * @param exception a possible thrown exception
     */
    public OperationResult(State resultState, Long affectedEntitiesCount,
            String message, Exception exception) {
        this(resultState, affectedEntitiesCount, message, "", null, exception);
    }

    /**
     * Create a new operation result object.
     *
     * @param resultState the result state
     * @param message an additional description message (possible error message)
     */
    public OperationResult(State resultState, String message) {
        this(resultState, 0l, message, "", null);
    }

    /**
     * Create a new operation result object.
     *
     * @param resultState the result state
     * @param message an additional description message (possible error message)
     * @param exception a possible thrown exception
     */
    public OperationResult(State resultState, String message,
            Exception exception) {
    	
    	this(resultState, 0l, message, "", null, exception);
    	
        //this(resultState, 0l, message, null, exception);
    }

    /**
     * Create a new operation result object.
     *
     * @param resultState the result state
     * @param message an additional description message (possible error message)
     * @param messageKey a template localization key of the message
     */
    public OperationResult(State resultState, String message, String messageKey) {
        this(resultState, 0l, message, messageKey, null);
    }

    /**
     * Create a new operation result object.
     *
     * @param resultState the result state
     * @param affectedEntitiesCount the number of affected entities
     * @param message an additional description message (possible error message)
     * @param messageKey a template localization key of the message
     */
    public OperationResult(State resultState, Long affectedEntitiesCount, String message, String messageKey) {
        this(resultState, affectedEntitiesCount, message, messageKey, null, null);
    }

    /**
     * Create a new operation result object.
     *
     * @param resultState the result state
     * @param affectedEntitiesCount the number of affected entities
     * @param message an additional description message (possible error message)
     * @param messageKey a template localization key of the message
     * @param affectedObject the object that was affected by the operation
     */
    public OperationResult(State resultState, Long affectedEntitiesCount, String message, String messageKey, PersistentObject affectedObject) {
        this(resultState, affectedEntitiesCount, message, messageKey, affectedObject, null);
    }

    /**
     * Create a new operation result object.
     *
     * @param resultState the result state
     * @param affectedEntitiesCount the number of affected entities
     * @param message an additional description message (possible error message)
     * @param messageKey a template localization key of t
     * @param affectedObject the object that was affected by the operation
     * @param exception a possible thrown exception
     */
    public OperationResult(State resultState, Long affectedEntitiesCount,
            String message, String messageKey, PersistentObject affectedObject, Exception exception) {
        this.resultState = resultState;
        this.affectedEntitiesCount = affectedEntitiesCount;
        this.message = message;
        this.messageKey = messageKey;
        this.exception = exception;
        this.affectedObject = affectedObject;
    }

    private State resultState;
    private String operationType;
    private String handledType;
    private Long affectedEntitiesCount;
    private String message;
    private String messageKey;
    private Exception exception;
    private PersistentObject affectedObject;

    /**
     * Get the result state of the operation.
     *
     * @return the result state
     */
    public State getResultState() {
        return this.resultState;
    }
    
    /**
     * Set the result state of the operation.
     * @param resultState the result state
     */
    public void setResultState(State resultState) {
    	this.resultState = resultState;
    }

    /**
     * Get the type of the executed operation.
     * @return the operation type
     */
    public String getOperationType() {
    	return this.operationType;
    }
    
    /**
     * Set the type of the executed operation.
     * @param type the operation type
     */
    public void setOperationType(String type) {
    	this.operationType = type;
    }
    
    /**
     * Get the type of the handled object.
     * @return the handled type
     */
    public String getHandledType() {
    	return this.handledType;
    }
    
    /**
     * Set the type of the handled object.
     * @param handledType the handled type
     */
    public void setHandledType(String handledType) {
    	this.handledType = handledType;
    }
    
    /**
     * Get the number of entities that were affected by the operation.
     *
     * @return the number of entities
     */
    public Long getAffectedEntitiesCount() {
        return this.affectedEntitiesCount;
    }
    
    /**
     * Set the numer of entities that were affected by the operation.
     * @param count the number of entities
     */
    public void setAffectedEntitiesCount(Long count) {
    	this.affectedEntitiesCount = count;
    }

    /**
     * Get an additional message to describe the operation result. May be used
     * for error messages.
     *
     * @return the additional message
     */
    public String getMessage() {
        return this.message;
    }
    
    /**
     * Set an additional message to describe the operation result. May be used for error messages.
     * @param message the additional message
     */
    public void setMessage(String message) {
    	this.message = message;
    }

    /**
     * Get a key for the additional message that could be used for template
     * localization purposes.
     *
     * @return the message key
     */
    public String getMessageKey() {
        return this.messageKey;
    }
    
    /**
     * Set a key for the additional message that could be used for template localization purposes.
     * @param key the message key
     */
    public void setMessageKey(String key) {
    	this.messageKey = key;
    }

    /**
     * Get the exception object that may be thrown within the operations
     * execution process.
     *
     * @return the exception object
     */
    public Exception getException() {
        return this.exception;
    }
    
    /**
     * Set the exception object that may be thrown within the operations execution process.
     * @param e the exception object
     */
    public void setException(Exception e) {
    	this.exception = e;
    }

    /**
     * Get the affected object that may be attached to this result.
     *
     * @return the affected object
     */
    public PersistentObject getAffectedObject() {
        return affectedObject;
    }
    
    /**
     * Set the affected object that may be attached to this result.
     * @param affectedObject the affected object
     */
    public void setAffectedObject(PersistentObject affectedObject) {
    	this.affectedObject = affectedObject;
    }

    /**
     * Returns true if the operation did not finish with the SUCCESSFULL state.
     *
     * @return whether the result has errors or not
     */
    public boolean hasErrors() {
        return !getResultState().equals(State.SUCCESSFULL);
    }
}
