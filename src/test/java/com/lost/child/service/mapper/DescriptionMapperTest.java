package com.lost.child.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DescriptionMapperTest {

    private DescriptionMapper descriptionMapper;

    @BeforeEach
    public void setUp() {
        descriptionMapper = new DescriptionMapperImpl();
    }
}
