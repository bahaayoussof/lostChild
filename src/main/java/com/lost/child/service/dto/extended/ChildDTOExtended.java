package com.lost.child.service.dto.extended;

import com.lost.child.domain.enumeration.Gender;
import com.lost.child.domain.enumeration.Status;
import com.lost.child.service.dto.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A DTO for the {@link com.lost.child.domain.Child} entity.
 */
public class ChildDTOExtended extends ChildDTO {

    private List<LastSeenDTO> lastSeens = new ArrayList<>();

    public List<LastSeenDTO> getLastSeens() {
        return lastSeens;
    }

    public void setLastSeens(List<LastSeenDTO> lastSeens) {
        this.lastSeens = lastSeens;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChildDTO{" +
            "id=" + getId() +
            ", image='" + getImage() + "'" +
            ", name='" + getName() + "'" +
            ", age=" + getAge() +
            ", gender='" + getGender() + "'" +
            ", birthdate='" + getBirthdate() + "'" +
            ", status='" + getStatus() + "'" +
            ", agency='" + getAgency() + "'" +
            ", address=" + getAddress() +
            ", description=" + getDescription() +
            ", contactInformation=" + getContactInformation() +
            ", user=" + getUser() +
            ", lastSeens" + getLastSeens() +
            "}";
    }
}
