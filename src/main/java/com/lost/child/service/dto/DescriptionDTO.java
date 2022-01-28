package com.lost.child.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.lost.child.domain.Description} entity.
 */
public class DescriptionDTO implements Serializable {

    private Long id;

    @NotNull
    private String eyeColor;

    @NotNull
    private String hairColor;

    @NotNull
    @Min(value = 1)
    @Max(value = 350)
    private Integer weight;

    @NotNull
    @Min(value = 1)
    @Max(value = 300)
    private Integer height;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(String eyeColor) {
        this.eyeColor = eyeColor;
    }

    public String getHairColor() {
        return hairColor;
    }

    public void setHairColor(String hairColor) {
        this.hairColor = hairColor;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DescriptionDTO)) {
            return false;
        }

        DescriptionDTO descriptionDTO = (DescriptionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, descriptionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DescriptionDTO{" +
            "id=" + getId() +
            ", eyeColor='" + getEyeColor() + "'" +
            ", hairColor='" + getHairColor() + "'" +
            ", weight=" + getWeight() +
            ", height=" + getHeight() +
            "}";
    }
}
