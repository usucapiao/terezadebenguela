package br.com.instituto.teresa.dto;

import br.com.instituto.teresa.domain.BoardMember;

public class BoardMemberResponseDTO {
    
    private Long id;
    private String role;
    private String name;

    public BoardMemberResponseDTO(BoardMember member) {
        this.id = member.getId();
        this.role = member.getRole();
        this.name = member.getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
