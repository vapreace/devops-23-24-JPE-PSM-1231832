package com.greglturnquist.payroll;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {
    @Test
    public void testEmployeeConstructor() {
        Employee employee = new Employee("John", "Doe", "Developer", "Software Engineer", 5);

        assertEquals("John", employee.getFirstName());
        assertEquals("Doe", employee.getLastName());
        assertEquals("Developer", employee.getDescription());
        assertEquals("Software Engineer", employee.getJobTitle());
        assertEquals(5, employee.getJobYears());
    }

    @Test
    public void testEquals() {
        Employee employee1 = new Employee("John", "Doe", "Developer", "Software Engineer", 5);
        Employee employee2 = new Employee("John", "Doe", "Developer", "Software Engineer", 5);
        Employee employee3 = new Employee("Jane", "Smith", "Manager", "Product Manager", 10);

        assertTrue(employee1.equals(employee2));
        assertFalse(employee1.equals(employee3));
    }

    @Test
    public void testHashCode() {
        Employee employee1 = new Employee("John", "Doe", "Developer", "Software Engineer", 5);
        Employee employee2 = new Employee("John", "Doe", "Developer", "Software Engineer", 5);

        assertEquals(employee1.hashCode(), employee2.hashCode());
    }

    @Test
    public void testToString() {
        Employee employee = new Employee("John", "Doe", "Developer", "Software Engineer", 5);
        String expectedString = "Employee{id=null, firstName='John', lastName='Doe', description='Developer', jobTitle='Software Engineer', jobYears=5}";

        assertEquals(expectedString, employee.toString());
    }
}