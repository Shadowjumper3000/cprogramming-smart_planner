package com.smartplanner.validation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Date validator implementation using DateFormat for validation.
 */
public class DateFormatValidator implements DateValidator {
    private final String dateFormat;

    /**
     * Creates a new DateFormatValidator with the specified date format.
     * 
     * @param dateFormat the date format string (e.g., "MM/dd/yyyy")
     */
    public DateFormatValidator(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    public boolean isValid(String dateStr) {
        DateFormat sdf = new SimpleDateFormat(this.dateFormat);
        sdf.setLenient(false);

        try {
            sdf.parse(dateStr);
            System.out.println("The date is in the correct format");
            return true;
        } catch (ParseException e) {
            System.out.println("The date isn't in the correct format");
            System.out.println(e);
            return false;
        }
    }
}