package com.lost.child.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.lost.child.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DescriptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Description.class);
        Description description1 = new Description();
        description1.setId(1L);
        Description description2 = new Description();
        description2.setId(description1.getId());
        assertThat(description1).isEqualTo(description2);
        description2.setId(2L);
        assertThat(description1).isNotEqualTo(description2);
        description1.setId(null);
        assertThat(description1).isNotEqualTo(description2);
    }
}
