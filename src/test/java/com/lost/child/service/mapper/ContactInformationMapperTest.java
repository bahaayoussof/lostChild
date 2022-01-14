package com.lost.child.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContactInformationMapperTest {

    private ContactInformationMapper contactInformationMapper;

    @BeforeEach
    public void setUp() {
        contactInformationMapper = new ContactInformationMapperImpl();
    }
}
