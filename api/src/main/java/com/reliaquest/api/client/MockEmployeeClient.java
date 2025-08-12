package com.reliaquest.api.client;

import com.reliaquest.api.model.ApiResponse;
import com.reliaquest.api.model.CreateEmployeeRequest;
import com.reliaquest.api.model.Employee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

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
@Slf4j
@Component
@RequiredArgsConstructor
public class MockEmployeeClient {
    private final WebClient webClient;

    public List<Employee> getAll() {
        return webClient.get().retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<List<Employee>>>() {
                })
                .map(ApiResponse::getData)
                .block();
    }

    public Employee getById(String id) {
        return webClient.get().uri("/{id}", id).retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<Employee>>() {
                })
                .map(ApiResponse::getData)
                .block();
    }

    public Employee create(CreateEmployeeRequest input) {
        var body = Map.of("name", input.getName(), "salary", input.getSalary(),
                "age", input.getAge(), "title", input.getTitle());
        return webClient.post().bodyValue(body).retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<Employee>>() {
                })
                .map(ApiResponse::getData)
                .block();
    }

    /**
     * NOTE: The mock server expects DELETE /employee/{name} with BODY { "name": "..." } and returns { "data": true }.
     */
    public boolean deleteByName(String name) {
        return webClient.method(HttpMethod.DELETE)
                .uri("/{name}", name)
                .bodyValue(Map.of("name", name))
                .retrieve()
                .onStatus(s -> s.value() == 404, resp -> Mono.empty())
                .onStatus(HttpStatusCode::isError, resp -> resp.createException().flatMap(Mono::error))
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<Boolean>>() {})
                .map(resp -> Boolean.TRUE.equals(resp.getData()))
                .defaultIfEmpty(false)
                .onErrorResume(ex -> Mono.just(false))
                .blockOptional()
                .orElse(false); // Avoid NPE
    }
}
