package com.smartplanner.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DateFormatValidator Tests")
class DateFormatValidatorTest {

  private DateFormatValidator validator;

  @BeforeEach
  void setUp() {
    validator = new DateFormatValidator("yyyy-MM-dd");
  }

  @Test
  @DisplayName("Should validate correct date format")
  void testValidDateFormat() {
    assertTrue(validator.isValid("2025-12-15"));
    assertTrue(validator.isValid("2024-01-01"));
    assertTrue(validator.isValid("2023-06-30"));
  }

  @Test
  @DisplayName("Should reject incorrect date format")
  void testInvalidDateFormat() {
    assertFalse(validator.isValid("15-12-2025")); // Wrong order
    assertFalse(validator.isValid("2025/12/15")); // Wrong separator
    assertFalse(validator.isValid("12-15-2025")); // MM-dd-yyyy format
  }

  @Test
  @DisplayName("Should reject invalid dates")
  void testInvalidDates() {
    assertFalse(validator.isValid("2025-13-01")); // Invalid month
    assertFalse(validator.isValid("2025-02-30")); // Invalid day for February
    assertFalse(validator.isValid("2025-00-01")); // Month 0
  }

  @Test
  @DisplayName("Should reject null input")
  void testNullInput() {
    // DateFormatValidator doesn't handle null - it throws NullPointerException
    assertThrows(NullPointerException.class, () -> validator.isValid(null));
  }

  @Test
  @DisplayName("Should reject empty string")
  void testEmptyString() {
    assertFalse(validator.isValid(""));
    assertFalse(validator.isValid("   "));
  }

  @Test
  @DisplayName("Should reject completely invalid input")
  void testInvalidInput() {
    assertFalse(validator.isValid("not-a-date"));
    assertFalse(validator.isValid("abc-def-ghi"));
    assertFalse(validator.isValid("12345"));
  }

  @Test
  @DisplayName("Should handle leap years correctly")
  void testLeapYears() {
    assertTrue(validator.isValid("2024-02-29")); // 2024 is leap year
    assertFalse(validator.isValid("2023-02-29")); // 2023 is not leap year
  }

  @Test
  @DisplayName("Should work with different date patterns")
  void testDifferentPatterns() {
    DateFormatValidator mmddyyyyValidator = new DateFormatValidator("MM/dd/yyyy");
    assertTrue(mmddyyyyValidator.isValid("12/15/2025"));
    assertFalse(mmddyyyyValidator.isValid("2025-12-15"));

    DateFormatValidator ddmmyyyyValidator = new DateFormatValidator("dd-MM-yyyy");
    assertTrue(ddmmyyyyValidator.isValid("15-12-2025"));
    assertFalse(ddmmyyyyValidator.isValid("2025-12-15"));
  }
}
