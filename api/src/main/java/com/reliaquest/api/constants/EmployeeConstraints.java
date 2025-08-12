package com.reliaquest.api.constants;

/**
 * Constraints for {@link com.reliaquest.api.model.CreateEmployeeRequest}
 * Alternatively, the constraints could be specified in a configuration class
 * then added to application.yml. A custom annotation could be used to enforce
 * along with a custom validator.
 *
 * Example:
 * <pre>
 * @ConfigurationProperties(prefix = "employee.validation")
 * public class EmployeeValidationProperties {
 *     private int minAge;
 *     private int maxAge;
 *     private int minSalary;
 *     // getters/setters
 * }
 *
 * application.yml:
 * employee:
 *  validation:
 *     minAge: 16
 *     maxAge: 75
 *     minSalary: 1
 *
 * Custom annotation:
 * @Target(ElementType.FIELD)
 * @Retention(RetentionPolicy.RUNTIME)
 * public @interface EmployeeConstraint {
 *     int minAge() default 16;
 *     int maxAge() default 75;
 *     int minSalary() default 1;
 * }
 *
 * Custom validator:
 * public class EmployeeConstraintValidator implements ConstraintValidator<EmployeeConstraint, CreateEmployeeRequest> {
 *     @Override
 *     public void initialize(EmployeeConstraint constraintAnnotation) {
 *         // no-op
 *     }
 *
 *     @Override
 *     public boolean isValid(CreateEmployeeRequest value, ConstraintValidatorContext context) {
 *         return value.getAge() >= constraintAnnotation.minAge() &&
 *                value.getAge() <= constraintAnnotation.maxAge() &&
 *                value.getSalary() >= constraintAnnotation.minSalary();
 *     }
 * }
 * </pre>
 */
public final class EmployeeConstraints {
    public static final int MIN_AGE = 16;
    public static final int MAX_AGE = 75;
    public static final int MIN_SALARY = 1;

    private EmployeeConstraints() {}
}
