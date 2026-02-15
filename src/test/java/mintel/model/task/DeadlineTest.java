package mintel.model.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import mintel.exception.InvalidDateFormatException;

/**
 * Test class for Deadline task, focusing on date validation.
 */
public class DeadlineTest {

    @Test
    public void testValidDates_yyyyMMdd() throws InvalidDateFormatException {
        Deadline deadline = new Deadline("Test task", "2026-03-15");
        assertEquals("2026-03-15", deadline.getByDate().toString());
        assertEquals("Mar 15 2026", deadline.displayBy);
    }

    @Test
    public void testValidDates_MMMddyyyy() throws InvalidDateFormatException {
        Deadline deadline = new Deadline("Test task", "Mar 15 2026");
        assertEquals("2026-03-15", deadline.getByDate().toString());
        assertEquals("Mar 15 2026", deadline.displayBy);
    }

    @Test
    public void testValidDates_flexibleDay() throws InvalidDateFormatException {
        Deadline deadline = new Deadline("Test task", "Mar 5 2026");
        assertEquals("2026-03-05", deadline.getByDate().toString());
        assertEquals("Mar 05 2026", deadline.displayBy);
    }

    @Test
    public void testInvalidDate_feb29NonLeapYear() {
        Exception exception = assertThrows(InvalidDateFormatException.class, () -> {
            new Deadline("Test task", "2026-02-29");
        });
        assertTrue(exception.getMessage().contains(""));
    }

    @Test
    public void testInvalidDate_feb29NonLeapYearWordFormat() {
        Exception exception = assertThrows(InvalidDateFormatException.class, () -> {
            new Deadline("Test task", "Feb 29 2026");
        });
        assertTrue(exception.getMessage().contains(""));
    }

    @Test
    public void testInvalidDate_apr31() {
        Exception exception = assertThrows(InvalidDateFormatException.class, () -> {
            new Deadline("Test task", "2026-04-31");
        });
        assertTrue(exception.getMessage().contains(""));
    }

    @Test
    public void testInvalidDate_feb30() {
        Exception exception = assertThrows(InvalidDateFormatException.class, () -> {
            new Deadline("Test task", "2026-02-30");
        });
        assertTrue(exception.getMessage().contains(""));
    }

    @Test
    public void testInvalidDate_jun31() {
        Exception exception = assertThrows(InvalidDateFormatException.class, () -> {
            new Deadline("Test task", "Jun 31 2026");
        });
        assertTrue(exception.getMessage().contains(""));
    }

    @Test
    public void testValidDate_feb29LeapYear() throws InvalidDateFormatException {
        Deadline deadline = new Deadline("Test task", "2024-02-29");
        assertEquals("2024-02-29", deadline.getByDate().toString());
        assertEquals("Feb 29 2024", deadline.displayBy);
    }

    @Test
    public void testInvalidFormat_unknown() {
        Exception exception = assertThrows(InvalidDateFormatException.class, () -> {
            new Deadline("Test task", "2026/03/15");
        });
        assertTrue(exception.getMessage().contains(""));
    }

    @Test
    public void testInvalidFormat_gibberish() {
        Exception exception = assertThrows(InvalidDateFormatException.class, () -> {
            new Deadline("Test task", "not a date");
        });
        assertTrue(exception.getMessage().contains(""));
    }

    @Test
    public void testEmptyDate() {
        Exception exception = assertThrows(InvalidDateFormatException.class, () -> {
            new Deadline("Test task", "");
        });
        assertTrue(exception.getMessage().contains(""));
    }
}