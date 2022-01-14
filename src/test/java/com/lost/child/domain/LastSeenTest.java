package com.lost.child.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.lost.child.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LastSeenTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LastSeen.class);
        LastSeen lastSeen1 = new LastSeen();
        lastSeen1.setId(1L);
        LastSeen lastSeen2 = new LastSeen();
        lastSeen2.setId(lastSeen1.getId());
        assertThat(lastSeen1).isEqualTo(lastSeen2);
        lastSeen2.setId(2L);
        assertThat(lastSeen1).isNotEqualTo(lastSeen2);
        lastSeen1.setId(null);
        assertThat(lastSeen1).isNotEqualTo(lastSeen2);
    }
}
