package com.reliaquest.api.controller;

import com.reliaquest.api.model.CreateEmployeeRequest;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/employees")
public class EmployeeController implements IEmployeeController<Employee, CreateEmployeeRequest> {
    private final EmployeeService service;

    @Override
    public ResponseEntity<List<Employee>> getAllEmployees() {
        var result = service.getAll();
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(@PathVariable String searchString) {
        var result = service.searchByName(searchString);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Employee> getEmployeeById(@PathVariable String id) {
        var e = service.getById(id);
        if (e == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(e);
    }

    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        return ResponseEntity.ok(service.highestSalary());
    }

    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        return ResponseEntity.ok(service.top10NamesBySalary());
    }

    @Override
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody CreateEmployeeRequest employeeInput) {
        var created = service.create(employeeInput);
        return ResponseEntity.ok(created);
    }

    @Override
    public ResponseEntity<String> deleteEmployeeById(@PathVariable String id) {
        String name = service.deleteByIdReturnName(id);
        return ResponseEntity.ok(name);
    }
}
