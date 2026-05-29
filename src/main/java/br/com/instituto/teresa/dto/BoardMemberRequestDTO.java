package br.com.instituto.teresa.dto;

import jakarta.validation.constraints.NotBlank;

public class BoardMemberRequestDTO {

    @NotBlank(message = "O cargo não pode estar vazio")
    private String role;

    @NotBlank(message = "O nome não pode estar vazio")
    private String name;

    public BoardMemberRequestDTO() {}

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
