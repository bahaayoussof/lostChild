package com.lost.child.service.dto;

import com.lost.child.domain.enumeration.Gender;
import com.lost.child.domain.enumeration.Status;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.lost.child.domain.Child} entity.
 */
public class ChildDTO implements Serializable {

    private Long id;

    @Lob
    private byte[] image;

    private String imageContentType;

    @NotNull
    @Size(min = 3, max = 50)
    private String name;

    @NotNull
    @Min(value = 1)
    @Max(value = 18)
    private Integer age;

    @NotNull
    private Gender gender;

    @NotNull
    private LocalDate birthdate;

    @NotNull
    private Status status;

    @Size(min = 3, max = 5)
    private String agency;

    private AddressDTO address;

    private DescriptionDTO description;

    private ContactInformationDTO contactInformation;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }

    public DescriptionDTO getDescription() {
        return description;
    }

    public void setDescription(DescriptionDTO description) {
        this.description = description;
    }

    public ContactInformationDTO getContactInformation() {
        return contactInformation;
    }

    public void setContactInformation(ContactInformationDTO contactInformation) {
        this.contactInformation = contactInformation;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChildDTO)) {
            return false;
        }

        ChildDTO childDTO = (ChildDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, childDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
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
            "}";
    }
}
