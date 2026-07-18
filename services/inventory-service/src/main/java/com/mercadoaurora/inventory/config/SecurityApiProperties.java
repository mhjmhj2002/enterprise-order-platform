package com.mercadoaurora.inventory.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "security.api")
public record SecurityApiProperties(@NotBlank String username, @NotBlank String password) {
}
