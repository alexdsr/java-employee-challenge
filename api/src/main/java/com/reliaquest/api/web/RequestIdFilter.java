package com.reliaquest.api.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Servlet filter that generates and attaches a unique request identifier (Request ID)
 * to each incoming HTTP request for logging and tracing purposes.
 * <p>
 * The Request ID is stored in the Mapped Diagnostic Context (MDC), allowing it to be
 * automatically included in log messages for the lifetime of the request. This makes it
 * easier to trace logs that belong to the same request across different application layers
 * or microservices.
 * </p>
 *
 * <p>
 * Workflow:
 * <ol>
 *   <li>When a request arrives, the filter generates a unique ID (e.g., a UUID).</li>
 *   <li>The ID is placed in the MDC under a known key (e.g., {@code requestId}).</li>
 *   <li>Downstream code and log statements can reference this MDC key to include the Request ID
 *       in log output automatically via the logging framework's pattern layout.</li>
 *   <li>Once the request is processed, the MDC entry is cleared to prevent leakage across threads.</li>
 * </ol>
 * </p>
 *
 * <p>
 * Benefits:
 * <ul>
 *   <li>Improved observability: Makes it easier to trace requests end-to-end in logs.</li>
 *   <li>Supports distributed tracing when the Request ID is propagated to other services.</li>
 *   <li>Helps debug concurrent or high-volume systems by correlating related log entries.</li>
 * </ul>
 * </p>
 *
 * @author Alexander Davila
 * @see org.slf4j.MDC
 */    
 @Component
public class RequestIdFilter extends OncePerRequestFilter {
    private static final String HDR = "X-Request-Id";
    private static final String MDC_KEY = "requestId";

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        String id = Optional.ofNullable(req.getHeader(HDR)).orElse(UUID.randomUUID().toString());
        MDC.put(MDC_KEY, id);
        res.setHeader(HDR, id);
        try { chain.doFilter(req, res); }
        finally { MDC.remove(MDC_KEY); }
    }
}
