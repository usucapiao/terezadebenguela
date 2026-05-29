package br.com.instituto.teresa.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity // Mapeia a classe e relaciona a uma tabela do banco
@Table(name = "volunteers") // Define o nome da tabela como volunteers
public class Volunteer {

    @Id // Registra o campo como chave primária da tabela
    @GeneratedValue(strategy = GenerationType.IDENTITY) // O campo receberá o Id gerado pelo lado do banco de dados (AutoIncremento)
    private Long id;

    @NotBlank(message = "O nome é obrigatório") // O Spring Validation garante que o valor não seja nulo, nem composto apenas de espaços vazios
    private String name;

    @NotBlank(message = "O e-mail é obrigatório") // Validação de campo não nulo e não vazio
    @Email(message = "E-mail com formato inválido") // Valida se é um email padrão com domínio (@) e sem caracteres ilegais
    private String email;

    private String phone;
    
    private String age;

    @NotBlank(message = "A motivação é obrigatória") // Define preenchimento obrigatório para motivação
    @Column(columnDefinition = "TEXT") // O sistema salvará no banco usando o tipo TEXT invés do tradicional VARCHAR(255) do JPA Padrão
    private String motivation;

    public Volunteer() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAge() { return age; }
    public void setAge(String age) { this.age = age; }
    public String getMotivation() { return motivation; }
    public void setMotivation(String motivation) { this.motivation = motivation; }
}
