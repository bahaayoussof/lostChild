package com.lost.child.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.lost.child.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContactInformationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactInformation.class);
        ContactInformation contactInformation1 = new ContactInformation();
        contactInformation1.setId(1L);
        ContactInformation contactInformation2 = new ContactInformation();
        contactInformation2.setId(contactInformation1.getId());
        assertThat(contactInformation1).isEqualTo(contactInformation2);
        contactInformation2.setId(2L);
        assertThat(contactInformation1).isNotEqualTo(contactInformation2);
        contactInformation1.setId(null);
        assertThat(contactInformation1).isNotEqualTo(contactInformation2);
    }
}
