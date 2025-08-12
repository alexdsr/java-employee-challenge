package com.reliaquest.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * Generic wrapper for API responses from the Mock Employee API (or other APIs that follow
 * the same response envelope pattern).
 * <p>
 * The Mock Employee API returns all results wrapped in a JSON object containing:
 * <ul>
 *   <li>{@code data} – the actual payload, which can be a single object, a list, or a primitive
 *       type (e.g., {@code Boolean}, {@code Integer}).</li>
 *   <li>{@code status} – a human-readable status message, typically indicating success.</li>
 *   <li>{@code error} – an optional error message if the request failed.</li>
 * </ul>
 *
 * <p>Example of a single employee response:
 * <pre>
 * {
 *   "data": {
 *     "id": "5255f1a5-f9f7-4be5-829a-134bde088d17",
 *     "employee_name": "Bill Bob",
 *     "employee_salary": 89750,
 *     "employee_age": 24,
 *     "employee_title": "Documentation Engineer",
 *     "employee_email": "billBob@company.com"
 *   },
 *   "status": "Successfully processed request."
 * }
 * </pre>
 *
 * <p>Example of a list response:
 * <pre>
 * {
 *   "data": [
 *     { "id": "...", "employee_name": "...", ... },
 *     { "id": "...", "employee_name": "...", ... }
 *   ],
 *   "status": "Successfully processed request."
 * }
 * </pre>
 *
 * <p>Usage example with {@link org.springframework.web.reactive.function.client.WebClient}:
 * <pre>
 * ApiResponse&lt;Employee&gt; response = webClient.get()
 *     .uri("/{id}", id)
 *     .retrieve()
 *     .bodyToMono(new ParameterizedTypeReference&lt;ApiResponse&lt;Employee&gt;&gt;() {})
 *     .block();
 * Employee employee = response.getData();
 * </pre>
 *
 * @param <T> the type of the {@code data} field, e.g., {@code Employee}, {@code List<Employee>}, {@code Boolean}, etc.
 *
 * @author Alexander Davila
 * @see com.reliaquest.api.client.MockEmployeeClient
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private T data;
    private String status;

    @JsonProperty("error")
    private String error;
}
