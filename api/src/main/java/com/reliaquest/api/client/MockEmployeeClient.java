package com.reliaquest.api.client;

import com.reliaquest.api.model.ApiResponse;
import com.reliaquest.api.model.CreateEmployeeRequest;
import com.reliaquest.api.model.Employee;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Request payload object used for creating a new {@code Employee} via the API.
 * <p>
 * This class represents the inbound data expected in the request body when
 * calling {@link com.reliaquest.api.controller.IEmployeeController#createEmployee(Object)}.
 * It contains only the fields that a client must supply to create an employee,
 * and uses Bean Validation annotations to enforce input rules at the API boundary.
 *
 * <p>Validation constraints:
 * <ul>
 *   <li>{@code name} – must not be blank</li>
 *   <li>{@code salary} – must be a positive integer</li>
 *   <li>{@code age} – must be between 16 and 75 inclusive</li>
 *   <li>{@code title} – must not be blank</li>
 * </ul>
 *
 * <p>Example JSON request body:
 * <pre>
 * {
 *   "name": "Jill Jenkins",
 *   "salary": 139082,
 *   "age": 48,
 *   "title": "Financial Advisor"
 * }
 * </pre>
 *
 * <p>Usage example with {@link org.springframework.web.reactive.function.client.WebClient}:
 * <pre>
 * CreateEmployeeRequest request = new CreateEmployeeRequest("Jill Jenkins", 139082, 48, "Financial Advisor");
 * ApiResponse&lt;Employee&gt; response = webClient.post()
 *     .bodyValue(request)
 *     .retrieve()
 *     .bodyToMono(new ParameterizedTypeReference&lt;ApiResponse&lt;Employee&gt;&gt;() {})
 *     .block();
 * </pre>
 *
 * @author Alexander Davila
 * @see com.reliaquest.api.model.CreateEmployeeRequest
 */
@Component
public class MockEmployeeClient {
    private static final Logger log = LoggerFactory.getLogger(MockEmployeeClient.class);

    private final WebClient webClient;


    public MockEmployeeClient(WebClient employeeWebClient) {
        this.webClient = employeeWebClient;
    }

    public List<Employee> getAll() {
        List<Employee> out = webClient.get()
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<List<Employee>>>() {})
                .map(ApiResponse::getData)
                .doOnSuccess(list -> log.info("Fetched {} employees", list == null ? 0 : list.size()))
                .timeout(Duration.ofSeconds(5))
                .onErrorResume(ex -> {
                    log.error("Failed to fetch employees: {}", ex.toString(), ex);
                    return Mono.just(List.of());
                })
                .block();
        return out == null ? List.of() : out;
    }

    public Employee getById(String id) {
        return webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<Employee>>() {})
                .map(ApiResponse::getData)
                .doOnSuccess(emp -> log.info("Fetched employee id={} found={}", id, emp != null))
                .timeout(Duration.ofSeconds(5))
                .doOnError(ex -> log.warn("Failed to fetch employee id={}: {}", id, ex.toString()))
                .onErrorResume(ex -> Mono.empty())
                .block();
    }

    public Employee create(CreateEmployeeRequest req) {
        return webClient.post()
                .bodyValue(Map.of(
                        "name", req.getName(),
                        "salary", req.getSalary(),
                        "age", req.getAge(),
                        "title", req.getTitle()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<Employee>>() {})
                .map(ApiResponse::getData)
                .doOnSuccess(emp -> log.info("Created employee name={} success={}", req.getName(), emp != null))
                .timeout(Duration.ofSeconds(5))
                .doOnError(ex -> log.error("Create employee failed name={}: {}", req.getName(), ex.toString()))
                .onErrorResume(ex -> Mono.empty())
                .block();
    }

    /**
     * NOTE: The mock server expects DELETE /employee/{name} with BODY { "name": "..." } and returns { "data": true }.
     */
    public boolean deleteByName(String name) {
        Boolean ok = webClient.method(HttpMethod.DELETE)
                .uri("/{name}", name)
                .bodyValue(Map.of("name", name))
                .retrieve()
                .onStatus(s -> s.value() == 404, resp -> {
                    log.info("Delete name={} -> 404 (treat as not deleted)", name);
                    return Mono.empty();
                })
                .onStatus(HttpStatusCode::isError, resp -> resp.createException().flatMap(Mono::error))
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<Boolean>>() {})
                .map(r -> Boolean.TRUE.equals(r.getData()))
                .defaultIfEmpty(false)
                .timeout(Duration.ofSeconds(5))
                .doOnSuccess(result -> log.info("Delete name={} result={}", name, result))
                .doOnError(ex -> log.warn("Delete name={} failed: {}", name, ex.toString()))
                .onErrorReturn(false)
                .block();
        return Boolean.TRUE.equals(ok);
    }
}
