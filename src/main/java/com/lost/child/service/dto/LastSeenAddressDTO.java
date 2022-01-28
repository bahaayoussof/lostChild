package com.lost.child.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.lost.child.domain.LastSeenAddress} entity.
 */
public class LastSeenAddressDTO implements Serializable {

    private Long id;

    @NotNull
    private String street;

    @NotNull
    private String city;

    private String state;

    private String country;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LastSeenAddressDTO)) {
            return false;
        }

        LastSeenAddressDTO lastSeenAddressDTO = (LastSeenAddressDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, lastSeenAddressDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LastSeenAddressDTO{" +
            "id=" + getId() +
            ", street='" + getStreet() + "'" +
            ", city='" + getCity() + "'" +
            ", state='" + getState() + "'" +
            ", country='" + getCountry() + "'" +
            "}";
    }
}
