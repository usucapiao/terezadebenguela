package br.com.instituto.teresa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequestDTO(
    @NotBlank String currentPassword,
    @NotBlank @Size(min = 8, message = "A nova senha deve ter no mínimo 8 caracteres") String newPassword
) {}
