package br.com.instituto.teresa.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record VolunteerRequestDTO(
    @NotBlank(message = "O nome é obrigatório") String name,
    @NotBlank(message = "O e-mail é obrigatório") @Email(message = "E-mail com formato inválido") String email,
    String phone,
    String age,
    @NotBlank(message = "A motivação é obrigatória") String motivation
) {}
