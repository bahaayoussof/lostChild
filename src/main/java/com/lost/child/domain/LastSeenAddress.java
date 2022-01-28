package com.lost.child.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LastSeenAddress.
 */
@Entity
@Table(name = "last_seen_address")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LastSeenAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "street", nullable = false)
    private String street;

    @NotNull
    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "country")
    private String country;

    @JsonIgnoreProperties(value = { "lastSeenAddress", "child" }, allowSetters = true)
    @OneToOne(mappedBy = "lastSeenAddress")
    private LastSeen lastSeen;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LastSeenAddress id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return this.street;
    }

    public LastSeenAddress street(String street) {
        this.setStreet(street);
        return this;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return this.city;
    }

    public LastSeenAddress city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return this.state;
    }

    public LastSeenAddress state(String state) {
        this.setState(state);
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return this.country;
    }

    public LastSeenAddress country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LastSeen getLastSeen() {
        return this.lastSeen;
    }

    public void setLastSeen(LastSeen lastSeen) {
        if (this.lastSeen != null) {
            this.lastSeen.setLastSeenAddress(null);
        }
        if (lastSeen != null) {
            lastSeen.setLastSeenAddress(this);
        }
        this.lastSeen = lastSeen;
    }

    public LastSeenAddress lastSeen(LastSeen lastSeen) {
        this.setLastSeen(lastSeen);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LastSeenAddress)) {
            return false;
        }
        return id != null && id.equals(((LastSeenAddress) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LastSeenAddress{" +
            "id=" + getId() +
            ", street='" + getStreet() + "'" +
            ", city='" + getCity() + "'" +
            ", state='" + getState() + "'" +
            ", country='" + getCountry() + "'" +
            "}";
    }
}
