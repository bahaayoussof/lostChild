package com.lost.child.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.lost.child.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContactInformationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactInformationDTO.class);
        ContactInformationDTO contactInformationDTO1 = new ContactInformationDTO();
        contactInformationDTO1.setId(1L);
        ContactInformationDTO contactInformationDTO2 = new ContactInformationDTO();
        assertThat(contactInformationDTO1).isNotEqualTo(contactInformationDTO2);
        contactInformationDTO2.setId(contactInformationDTO1.getId());
        assertThat(contactInformationDTO1).isEqualTo(contactInformationDTO2);
        contactInformationDTO2.setId(2L);
        assertThat(contactInformationDTO1).isNotEqualTo(contactInformationDTO2);
        contactInformationDTO1.setId(null);
        assertThat(contactInformationDTO1).isNotEqualTo(contactInformationDTO2);
    }
}
