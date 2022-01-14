package com.lost.child.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LastSeenMapperTest {

    private LastSeenMapper lastSeenMapper;

    @BeforeEach
    public void setUp() {
        lastSeenMapper = new LastSeenMapperImpl();
    }
}
