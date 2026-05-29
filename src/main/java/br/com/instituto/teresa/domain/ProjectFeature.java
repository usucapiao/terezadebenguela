package br.com.instituto.teresa.domain;

import jakarta.persistence.Embeddable;

@Embeddable // Indica que esta classe será "embutida" como parte de outra entidade, não tendo tabela própria
public class ProjectFeature {
    private String icon;
    private String text;

    public ProjectFeature() {}

    public ProjectFeature(String icon, String text) {
        this.icon = icon;
        this.text = text;
    }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}
