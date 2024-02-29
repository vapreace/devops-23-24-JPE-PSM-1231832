package com.greglturnquist.payroll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {
    private String firstName;
    private String lastName;
    private String description;
    private String jobTitle;
    private int jobYears;
    private Employee validEmployee;

    @BeforeEach
    public void setUp() {
        firstName = "John";
        lastName = "Doe";
        description = "Developer";
        jobTitle = "Software Engineer";
        jobYears = 5;
        validEmployee = new Employee(firstName, lastName, description, jobTitle, jobYears);
    }

    @Test
    public void testEmployeeConstructorThrowsExceptionWhenFirstNameIsNull() {
        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new Employee(null, this.lastName, this.description, this.jobTitle, this.jobYears);
        });
    }

    @Test
    public void testEmployeeConstructorThrowsExceptionWhenFirstNameIsEmpty() {
        // Arrange
        String firstName = "";
        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new Employee(firstName, this.lastName, this.description, this.jobTitle, this.jobYears);
        });
    }

    @Test
    public void testEmployeeConstructorThrowsExceptionWhenLastNameIsNull() {
        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new Employee(this.firstName, null, this.description, this.jobTitle, this.jobYears);
        });
    }

    @Test
    public void testEmployeeConstructorThrowsExceptionWhenLastNameIsEmpty() {
        // Arrange
        String lastName = "";
        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new Employee(this.firstName, lastName, this.description, this.jobTitle, this.jobYears);
        });
    }

    @Test
    public void testEmployeeConstructorThrowsExceptionWhenDescriptionIsNull() {
        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new Employee(this.firstName, this.lastName, null, this.jobTitle, this.jobYears);
        });
    }

    @Test
    public void testEmployeeConstructorThrowsExceptionWhenDescriptionIsEmpty() {
        // Arrange
        String description = "";
        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new Employee(this.firstName, this.lastName, description, this.jobTitle, this.jobYears);
        });
    }

    @Test
    public void testEmployeeConstructorThrowsExceptionWhenJobTitleIsNull() {
        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new Employee(this.firstName, this.lastName, this.description, null, this.jobYears);
        });
    }

    @Test
    public void testEmployeeConstructorThrowsExceptionWhenJobTitleIsEmpty() {
        // Arrange
        String jobTitle = "";
        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new Employee(this.firstName, this.lastName, this.description, jobTitle, this.jobYears);
        });
    }

    @Test
    public void testEmployeeConstructorThrowsExceptionWhenJobYearsIsNegative() {
        // Arrange
        int jobYears = -1;
        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new Employee(this.firstName, this.lastName, this.description, this.jobTitle, jobYears);
        });
    }

    @Test
    public void testEmployeeConstructorWithValidParameters() {
        // Act and Assert
        assertDoesNotThrow(() -> {
            new Employee(firstName, lastName, description, jobTitle, jobYears);
        });
    }

    @Test
    public void testEmployeeConstructorAcceptsZeroJobYears() {
        // Arrange
        int jobYears = 0;
        // Act and Assert
        assertDoesNotThrow(() -> {
            new Employee(firstName, lastName, description, jobTitle, jobYears);
        });
    }

    @Test
    public void testEqualsTrue() {
        Employee validEmployee2 = new Employee(firstName, lastName, description, jobTitle, jobYears);
        // Act and Assert
        assertTrue(this.validEmployee.equals(validEmployee2));
    }

    @Test
    public void testNotEqualsFalse() {
        // Arrange
        Employee validEmployee2 = new Employee(firstName + "test", lastName, description, jobTitle, jobYears);
        // Act and Assert
        assertFalse(this.validEmployee.equals(validEmployee2) || validEmployee2.equals(validEmployee));
    }

    @Test
    public void testHashCodeTrue() {
        Employee validEmployee2 = new Employee(firstName, lastName, description, jobTitle, jobYears);
        // Act and Assert
        assertEquals(validEmployee.hashCode(), validEmployee2.hashCode());
    }

    @Test
    public void testHashCodeFalse() {
        // Arrange
        Employee validEmployee2 = new Employee(firstName + "test", lastName, description, jobTitle, jobYears);
        // Act and Assert
        assertNotEquals(validEmployee.hashCode(), validEmployee2.hashCode());
    }

    @Test
    public void testSetId() {
        // Arrange
        Long expected = 1L;
        // Act
        validEmployee.setId(expected);
        // Assert
        assertEquals(expected, validEmployee.getId());
    }

    @Test
    public void testSetFirstNameThrowsExceptionWhenFirstNameIsNull() {
        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            validEmployee.setFirstName(null);
        });
    }

    @Test
    public void testSetFirstNameThrowsExceptionWhenFirstNameIsEmpty() {
        // Arrange
        String firstName = "";
        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            validEmployee.setFirstName(firstName);
        });
    }

    @Test
    public void testSetFirstNameWithValidParameters() {
        // Act and Assert
        assertDoesNotThrow(() -> {
            validEmployee.setFirstName(firstName);
        });
    }

    @Test
    public void testSetLastNameThrowsExceptionWhenLastNameIsNull() {
        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            validEmployee.setLastName(null);
        });
    }

    @Test
    public void testSetLastNameThrowsExceptionWhenLastNameIsEmpty() {
        // Arrange
        String lastName = "";
        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            validEmployee.setLastName(lastName);
        });
    }

    @Test
    public void testSetLastNameWithValidParameters() {
        // Act and Assert
        assertDoesNotThrow(() -> {
            validEmployee.setLastName(lastName);
        });
    }

    @Test
    public void testSetDescriptionThrowsExceptionWhenDescriptionIsNull() {
        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            validEmployee.setDescription(null);
        });
    }

    @Test
    public void testSetDescriptionThrowsExceptionWhenDescriptionIsEmpty() {
        // Arrange
        String description = "";
        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            validEmployee.setDescription(description);
        });
    }

    @Test
    public void testSetDescriptionWithValidParameters() {
        // Act and Assert
        assertDoesNotThrow(() -> {
            validEmployee.setDescription(description);
        });
    }

    @Test
    public void testSetJobTitleThrowsExceptionWhenJobTitleIsNull() {
        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            validEmployee.setJobTitle(null);
        });
    }

    @Test
    public void testSetJobTitleThrowsExceptionWhenJobTitleIsEmpty() {
        // Arrange
        String jobTitle = "";
        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            validEmployee.setJobTitle(jobTitle);
        });
    }

    @Test
    public void testSetJobTitleWithValidParameters() {
        // Act and Assert
        assertDoesNotThrow(() -> {
            validEmployee.setJobTitle(jobTitle);
        });
    }

    @Test
    public void testSetJobYearsThrowsExceptionWhenJobYearsIsNegative() {
        // Arrange
        int jobYears = -1;
        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            validEmployee.setJobYears(jobYears);
        });
    }

    @Test
    public void testSetJobYearsWithValidParameters() {
        // Act and Assert
        assertDoesNotThrow(() -> {
            validEmployee.setJobYears(jobYears);
        });
    }

    @Test
    public void testToString() {
        // Arrange
        String expected = "Employee{" +
            "id=" + validEmployee.getId() +
            ", firstName='" + validEmployee.getFirstName() + '\'' +
            ", lastName='" + validEmployee.getLastName() + '\'' +
            ", description='" + validEmployee.getDescription() + '\'' +
            ", jobTitle='" + validEmployee.getJobTitle() + '\'' +
            ", jobYears=" + validEmployee.getJobYears() +
            '}';
        // Act and Assert
        assertEquals(expected, validEmployee.toString());
    }
}