package com.reliaquest.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * Domain model representing an employee record within the system.
 * <p>
 * This class is used internally to represent employee data returned from
 * the Mock Employee API or persisted within the application. It is also
 * used in DTO mappings to {@link com.reliaquest.api.model.EmployeeResponse}.
 *
 * <p>It includes Jackson annotations to map between the snake_case field
 * names used by the external API and the camelCase convention in Java.
 *
 * <p>Example of external API JSON mapping:
 * <pre>
 * {
 *   "id": "5255f1a5-f9f7-4be5-829a-134bde088d17",
 *   "employee_name": "Bill Bob",
 *   "employee_salary": 89750,
 *   "employee_age": 24,
 *   "employee_title": "Documentation Engineer",
 *   "employee_email": "billBob@company.com"
 * }
 * </pre>
 *
 * @param id     the unique identifier of the employee
 * @param name   the full name of the employee (mapped from {@code employee_name})
 * @param salary the salary of the employee (mapped from {@code employee_salary})
 * @param age    the age of the employee (mapped from {@code employee_age})
 * @param title  the job title of the employee (mapped from {@code employee_title})
 * @param email  the email address of the employee (mapped from {@code employee_email})
 *
 * @author Alexander Davila
 *
 * @see com.reliaquest.api.model.CreateEmployeeRequest
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Employee {
    private String id;

    @JsonProperty("employee_name")
    private String name;

    @JsonProperty("employee_salary")
    private Integer salary;

    @JsonProperty("employee_age")
    private Integer age;

    @JsonProperty("employee_title")
    private String title;

    @JsonProperty("employee_email")
    private String email;
}
