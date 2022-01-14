package com.lost.child.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lost.child.domain.enumeration.Gender;
import com.lost.child.domain.enumeration.Status;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Child.
 */
@Entity
@Table(name = "child")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Child implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @NotNull
    @Size(min = 3, max = 50)
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @NotNull
    @Min(value = 1)
    @Max(value = 18)
    @Column(name = "age", nullable = false)
    private Integer age;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @NotNull
    @Column(name = "birthdate", nullable = false)
    private LocalDate birthdate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Size(min = 3, max = 5)
    @Column(name = "agency", length = 5)
    private String agency;

    @JsonIgnoreProperties(value = { "child", "lastSeen" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Address address;

    @JsonIgnoreProperties(value = { "child" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Description description;

    @JsonIgnoreProperties(value = { "child" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private ContactInformation contactInformation;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "child")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "address", "child" }, allowSetters = true)
    private Set<LastSeen> lastSeens = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Child id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getImage() {
        return this.image;
    }

    public Child image(byte[] image) {
        this.setImage(image);
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public Child imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getName() {
        return this.name;
    }

    public Child name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return this.age;
    }

    public Child age(Integer age) {
        this.setAge(age);
        return this;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Gender getGender() {
        return this.gender;
    }

    public Child gender(Gender gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getBirthdate() {
        return this.birthdate;
    }

    public Child birthdate(LocalDate birthdate) {
        this.setBirthdate(birthdate);
        return this;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public Status getStatus() {
        return this.status;
    }

    public Child status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getAgency() {
        return this.agency;
    }

    public Child agency(String agency) {
        this.setAgency(agency);
        return this;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public Address getAddress() {
        return this.address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Child address(Address address) {
        this.setAddress(address);
        return this;
    }

    public Description getDescription() {
        return this.description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public Child description(Description description) {
        this.setDescription(description);
        return this;
    }

    public ContactInformation getContactInformation() {
        return this.contactInformation;
    }

    public void setContactInformation(ContactInformation contactInformation) {
        this.contactInformation = contactInformation;
    }

    public Child contactInformation(ContactInformation contactInformation) {
        this.setContactInformation(contactInformation);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Child user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<LastSeen> getLastSeens() {
        return this.lastSeens;
    }

    public void setLastSeens(Set<LastSeen> lastSeens) {
        if (this.lastSeens != null) {
            this.lastSeens.forEach(i -> i.setChild(null));
        }
        if (lastSeens != null) {
            lastSeens.forEach(i -> i.setChild(this));
        }
        this.lastSeens = lastSeens;
    }

    public Child lastSeens(Set<LastSeen> lastSeens) {
        this.setLastSeens(lastSeens);
        return this;
    }

    public Child addLastSeen(LastSeen lastSeen) {
        this.lastSeens.add(lastSeen);
        lastSeen.setChild(this);
        return this;
    }

    public Child removeLastSeen(LastSeen lastSeen) {
        this.lastSeens.remove(lastSeen);
        lastSeen.setChild(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Child)) {
            return false;
        }
        return id != null && id.equals(((Child) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Child{" +
            "id=" + getId() +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", name='" + getName() + "'" +
            ", age=" + getAge() +
            ", gender='" + getGender() + "'" +
            ", birthdate='" + getBirthdate() + "'" +
            ", status='" + getStatus() + "'" +
            ", agency='" + getAgency() + "'" +
            "}";
    }
}
