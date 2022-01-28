package com.lost.child.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.lost.child.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LastSeenAddressTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LastSeenAddress.class);
        LastSeenAddress lastSeenAddress1 = new LastSeenAddress();
        lastSeenAddress1.setId(1L);
        LastSeenAddress lastSeenAddress2 = new LastSeenAddress();
        lastSeenAddress2.setId(lastSeenAddress1.getId());
        assertThat(lastSeenAddress1).isEqualTo(lastSeenAddress2);
        lastSeenAddress2.setId(2L);
        assertThat(lastSeenAddress1).isNotEqualTo(lastSeenAddress2);
        lastSeenAddress1.setId(null);
        assertThat(lastSeenAddress1).isNotEqualTo(lastSeenAddress2);
    }
}
