package com.lost.child.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.lost.child.domain.LastSeen} entity.
 */
public class LastSeenDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate date;

    private LastSeenAddressDTO lastSeenAddress;

    private ChildDTO child;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LastSeenAddressDTO getLastSeenAddress() {
        return lastSeenAddress;
    }

    public void setLastSeenAddress(LastSeenAddressDTO lastSeenAddress) {
        this.lastSeenAddress = lastSeenAddress;
    }

    public ChildDTO getChild() {
        return child;
    }

    public void setChild(ChildDTO child) {
        this.child = child;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LastSeenDTO)) {
            return false;
        }

        LastSeenDTO lastSeenDTO = (LastSeenDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, lastSeenDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LastSeenDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", lastSeenAddress=" + getLastSeenAddress() +
            ", child=" + getChild() +
            "}";
    }
}
