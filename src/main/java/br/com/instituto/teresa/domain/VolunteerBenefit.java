package br.com.instituto.teresa.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public class VolunteerBenefit {
    private String icon;
    private String title;
    private String description;

    public VolunteerBenefit() {}

    public VolunteerBenefit(String icon, String title, String description) {
        this.icon = icon;
        this.title = title;
        this.description = description;
    }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
