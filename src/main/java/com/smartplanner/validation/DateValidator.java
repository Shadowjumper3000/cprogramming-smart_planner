package com.smartplanner.validation;

/**
 * Interface for date validation implementations.
 */
public interface DateValidator {
    /**
     * Validates if a date string is in the correct format.
     * 
     * @param dateStr the date string to validate
     * @return true if the date is valid, false otherwise
     */
    boolean isValid(String dateStr);
}