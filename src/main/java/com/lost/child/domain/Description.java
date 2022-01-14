package com.lost.child.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Description.
 */
@Entity
@Table(name = "description")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Description implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "eye_color", nullable = false)
    private String eyeColor;

    @NotNull
    @Column(name = "hair_color", nullable = false)
    private String hairColor;

    @NotNull
    @Column(name = "weight", nullable = false)
    private Integer weight;

    @NotNull
    @Column(name = "height", nullable = false)
    private Integer height;

    @JsonIgnoreProperties(value = { "address", "description", "contactInformation", "user", "lastSeens" }, allowSetters = true)
    @OneToOne(mappedBy = "description")
    private Child child;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Description id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEyeColor() {
        return this.eyeColor;
    }

    public Description eyeColor(String eyeColor) {
        this.setEyeColor(eyeColor);
        return this;
    }

    public void setEyeColor(String eyeColor) {
        this.eyeColor = eyeColor;
    }

    public String getHairColor() {
        return this.hairColor;
    }

    public Description hairColor(String hairColor) {
        this.setHairColor(hairColor);
        return this;
    }

    public void setHairColor(String hairColor) {
        this.hairColor = hairColor;
    }

    public Integer getWeight() {
        return this.weight;
    }

    public Description weight(Integer weight) {
        this.setWeight(weight);
        return this;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getHeight() {
        return this.height;
    }

    public Description height(Integer height) {
        this.setHeight(height);
        return this;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Child getChild() {
        return this.child;
    }

    public void setChild(Child child) {
        if (this.child != null) {
            this.child.setDescription(null);
        }
        if (child != null) {
            child.setDescription(this);
        }
        this.child = child;
    }

    public Description child(Child child) {
        this.setChild(child);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Description)) {
            return false;
        }
        return id != null && id.equals(((Description) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Description{" +
            "id=" + getId() +
            ", eyeColor='" + getEyeColor() + "'" +
            ", hairColor='" + getHairColor() + "'" +
            ", weight=" + getWeight() +
            ", height=" + getHeight() +
            "}";
    }
}
