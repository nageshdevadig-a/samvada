package io.tharka.samvada.core.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.Instant;
import java.util.Map;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorDTO(
        String title,
        int status,
        String detail,
        String instance,
        Instant timestamp,
        Map<String, String> errors
) {}

