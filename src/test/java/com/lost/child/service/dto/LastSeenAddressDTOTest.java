package com.lost.child.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.lost.child.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LastSeenAddressDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LastSeenAddressDTO.class);
        LastSeenAddressDTO lastSeenAddressDTO1 = new LastSeenAddressDTO();
        lastSeenAddressDTO1.setId(1L);
        LastSeenAddressDTO lastSeenAddressDTO2 = new LastSeenAddressDTO();
        assertThat(lastSeenAddressDTO1).isNotEqualTo(lastSeenAddressDTO2);
        lastSeenAddressDTO2.setId(lastSeenAddressDTO1.getId());
        assertThat(lastSeenAddressDTO1).isEqualTo(lastSeenAddressDTO2);
        lastSeenAddressDTO2.setId(2L);
        assertThat(lastSeenAddressDTO1).isNotEqualTo(lastSeenAddressDTO2);
        lastSeenAddressDTO1.setId(null);
        assertThat(lastSeenAddressDTO1).isNotEqualTo(lastSeenAddressDTO2);
    }
}
