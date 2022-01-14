package com.lost.child.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.lost.child.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LastSeenDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LastSeenDTO.class);
        LastSeenDTO lastSeenDTO1 = new LastSeenDTO();
        lastSeenDTO1.setId(1L);
        LastSeenDTO lastSeenDTO2 = new LastSeenDTO();
        assertThat(lastSeenDTO1).isNotEqualTo(lastSeenDTO2);
        lastSeenDTO2.setId(lastSeenDTO1.getId());
        assertThat(lastSeenDTO1).isEqualTo(lastSeenDTO2);
        lastSeenDTO2.setId(2L);
        assertThat(lastSeenDTO1).isNotEqualTo(lastSeenDTO2);
        lastSeenDTO1.setId(null);
        assertThat(lastSeenDTO1).isNotEqualTo(lastSeenDTO2);
    }
}
