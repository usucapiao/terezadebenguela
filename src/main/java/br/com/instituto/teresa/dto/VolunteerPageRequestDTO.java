package br.com.instituto.teresa.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class VolunteerPageRequestDTO {

    @NotBlank(message = "O título 1 é obrigatório")
    private String title1;

    @NotBlank(message = "O título 2 é obrigatório")
    private String title2;

    @NotBlank(message = "A descrição é obrigatória")
    private String description;

    private List<VolunteerBenefitDTO> benefits;

    public VolunteerPageRequestDTO() {}

    public String getTitle1() { return title1; }
    public void setTitle1(String title1) { this.title1 = title1; }

    public String getTitle2() { return title2; }
    public void setTitle2(String title2) { this.title2 = title2; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<VolunteerBenefitDTO> getBenefits() { return benefits; }
    public void setBenefits(List<VolunteerBenefitDTO> benefits) { this.benefits = benefits; }
}
