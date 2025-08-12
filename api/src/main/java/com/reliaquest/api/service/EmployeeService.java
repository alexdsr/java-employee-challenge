package com.reliaquest.api.service;

import com.reliaquest.api.client.MockEmployeeClient;
import com.reliaquest.api.model.CreateEmployeeRequest;
import com.reliaquest.api.model.Employee;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final MockEmployeeClient client;

    public List<Employee> getAll() {
        return client.getAll();
    }

    public List<Employee> searchByName(String fragment) {
        var f = fragment == null ? "" : fragment.toLowerCase(Locale.ROOT);
        return client.getAll().stream()
                .filter(e -> e.getName() != null && e.getName().toLowerCase(Locale.ROOT).contains(f))
                .toList();
    }

    public Employee getById(String id) {
        return client.getById(id);
    }

    public Integer highestSalary() {
        return client.getAll().stream()
                .map(Employee::getSalary)
                .filter(Objects::nonNull)
                .max(Integer::compareTo)
                .orElse(0);
    }

    public List<String> top10NamesBySalary() {
        return client.getAll().stream()
                .sorted(Comparator.comparing(Employee::getSalary, Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .limit(10)
                .map(Employee::getName)
                .toList();
    }

    public Employee create(CreateEmployeeRequest input) {
        return client.create(input);
    }

    /**
     * API CONTRACT MISMATCH HANDLING:
     * The controller method is deleteEmployeeById(id), but the mock server deletes by NAME in the path.
     * Strategy:
     * 1) GET /employee/{id} to resolve name
     * 2) DELETE /employee/{name}
     * 3) Return the deleted name (or throw if missing/failed)
     */
    public String deleteByIdReturnName(String id) {
        var e = client.getById(id);
        if (e == null || e.getName() == null) {
            throw new IllegalArgumentException("Employee not found for id=" + id);
        }
        boolean ok = client.deleteByName(e.getName());
        if (!ok) {
            throw new IllegalStateException("Failed to delete employee name=" + e.getName());
        }
        return e.getName();
    }
}
