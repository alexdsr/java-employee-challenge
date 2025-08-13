package com.reliaquest.api.controller;

import com.reliaquest.api.model.CreateEmployeeRequest;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.service.EmployeeService;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employees")
public class EmployeeController implements IEmployeeController<Employee, CreateEmployeeRequest> {
    private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);
    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<List<Employee>> getAllEmployees() {
        log.info("Controller: GET /employees");
        return ResponseEntity.ok(service.getAll());
    }

    @Override
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(@PathVariable String searchString) {
        log.info("Controller: GET /employees/search/{}", searchString);
        return ResponseEntity.ok(service.searchByName(searchString));
    }

    @Override
    public ResponseEntity<Employee> getEmployeeById(@PathVariable String id) {
        log.info("Controller: GET /employees/{}", id);
        var e = service.getById(id);
        return e == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(e);
    }

    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        log.info("Controller: GET /employees/highestSalary");
        return ResponseEntity.ok(service.highestSalary());
    }

    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        log.info("Controller: GET /employees/topTenHighestEarningEmployeeNames");
        return ResponseEntity.ok(service.top10NamesBySalary());
    }

    @Override
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody CreateEmployeeRequest employeeInput) {
        log.info("Controller: POST /employees name={}", employeeInput.getName());
        return ResponseEntity.ok(service.create(employeeInput));
    }

    @Override
    public ResponseEntity<String> deleteEmployeeById(@PathVariable String id) {
        log.info("Controller: DELETE /employees/{}", id);
        return ResponseEntity.ok(service.deleteByIdReturnName(id));
    }
}
