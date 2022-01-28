package com.lost.child.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LastSeenAddressMapperTest {

    private LastSeenAddressMapper lastSeenAddressMapper;

    @BeforeEach
    public void setUp() {
        lastSeenAddressMapper = new LastSeenAddressMapperImpl();
    }
}
