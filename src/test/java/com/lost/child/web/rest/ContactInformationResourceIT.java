package com.lost.child.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.lost.child.IntegrationTest;
import com.lost.child.domain.ContactInformation;
import com.lost.child.repository.ContactInformationRepository;
import com.lost.child.service.dto.ContactInformationDTO;
import com.lost.child.service.mapper.ContactInformationMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ContactInformationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContactInformationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "/G@-l.oWxA1";
    private static final String UPDATED_EMAIL = "EkC@otEw.U_x!";

    private static final Integer DEFAULT_PHONE_NUMBER = 3;
    private static final Integer UPDATED_PHONE_NUMBER = 4;

    private static final String ENTITY_API_URL = "/api/contact-informations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContactInformationRepository contactInformationRepository;

    @Autowired
    private ContactInformationMapper contactInformationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContactInformationMockMvc;

    private ContactInformation contactInformation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactInformation createEntity(EntityManager em) {
        ContactInformation contactInformation = new ContactInformation()
            .name(DEFAULT_NAME)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER);
        return contactInformation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactInformation createUpdatedEntity(EntityManager em) {
        ContactInformation contactInformation = new ContactInformation()
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
        return contactInformation;
    }

    @BeforeEach
    public void initTest() {
        contactInformation = createEntity(em);
    }

    @Test
    @Transactional
    void createContactInformation() throws Exception {
        int databaseSizeBeforeCreate = contactInformationRepository.findAll().size();
        // Create the ContactInformation
        ContactInformationDTO contactInformationDTO = contactInformationMapper.toDto(contactInformation);
        restContactInformationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactInformationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ContactInformation in the database
        List<ContactInformation> contactInformationList = contactInformationRepository.findAll();
        assertThat(contactInformationList).hasSize(databaseSizeBeforeCreate + 1);
        ContactInformation testContactInformation = contactInformationList.get(contactInformationList.size() - 1);
        assertThat(testContactInformation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testContactInformation.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testContactInformation.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void createContactInformationWithExistingId() throws Exception {
        // Create the ContactInformation with an existing ID
        contactInformation.setId(1L);
        ContactInformationDTO contactInformationDTO = contactInformationMapper.toDto(contactInformation);

        int databaseSizeBeforeCreate = contactInformationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactInformationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactInformationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactInformation in the database
        List<ContactInformation> contactInformationList = contactInformationRepository.findAll();
        assertThat(contactInformationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactInformationRepository.findAll().size();
        // set the field null
        contactInformation.setName(null);

        // Create the ContactInformation, which fails.
        ContactInformationDTO contactInformationDTO = contactInformationMapper.toDto(contactInformation);

        restContactInformationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactInformationDTO))
            )
            .andExpect(status().isBadRequest());

        List<ContactInformation> contactInformationList = contactInformationRepository.findAll();
        assertThat(contactInformationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactInformationRepository.findAll().size();
        // set the field null
        contactInformation.setEmail(null);

        // Create the ContactInformation, which fails.
        ContactInformationDTO contactInformationDTO = contactInformationMapper.toDto(contactInformation);

        restContactInformationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactInformationDTO))
            )
            .andExpect(status().isBadRequest());

        List<ContactInformation> contactInformationList = contactInformationRepository.findAll();
        assertThat(contactInformationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactInformationRepository.findAll().size();
        // set the field null
        contactInformation.setPhoneNumber(null);

        // Create the ContactInformation, which fails.
        ContactInformationDTO contactInformationDTO = contactInformationMapper.toDto(contactInformation);

        restContactInformationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactInformationDTO))
            )
            .andExpect(status().isBadRequest());

        List<ContactInformation> contactInformationList = contactInformationRepository.findAll();
        assertThat(contactInformationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContactInformations() throws Exception {
        // Initialize the database
        contactInformationRepository.saveAndFlush(contactInformation);

        // Get all the contactInformationList
        restContactInformationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactInformation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @Test
    @Transactional
    void getContactInformation() throws Exception {
        // Initialize the database
        contactInformationRepository.saveAndFlush(contactInformation);

        // Get the contactInformation
        restContactInformationMockMvc
            .perform(get(ENTITY_API_URL_ID, contactInformation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contactInformation.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getNonExistingContactInformation() throws Exception {
        // Get the contactInformation
        restContactInformationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewContactInformation() throws Exception {
        // Initialize the database
        contactInformationRepository.saveAndFlush(contactInformation);

        int databaseSizeBeforeUpdate = contactInformationRepository.findAll().size();

        // Update the contactInformation
        ContactInformation updatedContactInformation = contactInformationRepository.findById(contactInformation.getId()).get();
        // Disconnect from session so that the updates on updatedContactInformation are not directly saved in db
        em.detach(updatedContactInformation);
        updatedContactInformation.name(UPDATED_NAME).email(UPDATED_EMAIL).phoneNumber(UPDATED_PHONE_NUMBER);
        ContactInformationDTO contactInformationDTO = contactInformationMapper.toDto(updatedContactInformation);

        restContactInformationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactInformationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactInformationDTO))
            )
            .andExpect(status().isOk());

        // Validate the ContactInformation in the database
        List<ContactInformation> contactInformationList = contactInformationRepository.findAll();
        assertThat(contactInformationList).hasSize(databaseSizeBeforeUpdate);
        ContactInformation testContactInformation = contactInformationList.get(contactInformationList.size() - 1);
        assertThat(testContactInformation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testContactInformation.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testContactInformation.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void putNonExistingContactInformation() throws Exception {
        int databaseSizeBeforeUpdate = contactInformationRepository.findAll().size();
        contactInformation.setId(count.incrementAndGet());

        // Create the ContactInformation
        ContactInformationDTO contactInformationDTO = contactInformationMapper.toDto(contactInformation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactInformationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactInformationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactInformationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactInformation in the database
        List<ContactInformation> contactInformationList = contactInformationRepository.findAll();
        assertThat(contactInformationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContactInformation() throws Exception {
        int databaseSizeBeforeUpdate = contactInformationRepository.findAll().size();
        contactInformation.setId(count.incrementAndGet());

        // Create the ContactInformation
        ContactInformationDTO contactInformationDTO = contactInformationMapper.toDto(contactInformation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactInformationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactInformationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactInformation in the database
        List<ContactInformation> contactInformationList = contactInformationRepository.findAll();
        assertThat(contactInformationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContactInformation() throws Exception {
        int databaseSizeBeforeUpdate = contactInformationRepository.findAll().size();
        contactInformation.setId(count.incrementAndGet());

        // Create the ContactInformation
        ContactInformationDTO contactInformationDTO = contactInformationMapper.toDto(contactInformation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactInformationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactInformationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactInformation in the database
        List<ContactInformation> contactInformationList = contactInformationRepository.findAll();
        assertThat(contactInformationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContactInformationWithPatch() throws Exception {
        // Initialize the database
        contactInformationRepository.saveAndFlush(contactInformation);

        int databaseSizeBeforeUpdate = contactInformationRepository.findAll().size();

        // Update the contactInformation using partial update
        ContactInformation partialUpdatedContactInformation = new ContactInformation();
        partialUpdatedContactInformation.setId(contactInformation.getId());

        partialUpdatedContactInformation.phoneNumber(UPDATED_PHONE_NUMBER);

        restContactInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactInformation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContactInformation))
            )
            .andExpect(status().isOk());

        // Validate the ContactInformation in the database
        List<ContactInformation> contactInformationList = contactInformationRepository.findAll();
        assertThat(contactInformationList).hasSize(databaseSizeBeforeUpdate);
        ContactInformation testContactInformation = contactInformationList.get(contactInformationList.size() - 1);
        assertThat(testContactInformation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testContactInformation.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testContactInformation.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void fullUpdateContactInformationWithPatch() throws Exception {
        // Initialize the database
        contactInformationRepository.saveAndFlush(contactInformation);

        int databaseSizeBeforeUpdate = contactInformationRepository.findAll().size();

        // Update the contactInformation using partial update
        ContactInformation partialUpdatedContactInformation = new ContactInformation();
        partialUpdatedContactInformation.setId(contactInformation.getId());

        partialUpdatedContactInformation.name(UPDATED_NAME).email(UPDATED_EMAIL).phoneNumber(UPDATED_PHONE_NUMBER);

        restContactInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactInformation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContactInformation))
            )
            .andExpect(status().isOk());

        // Validate the ContactInformation in the database
        List<ContactInformation> contactInformationList = contactInformationRepository.findAll();
        assertThat(contactInformationList).hasSize(databaseSizeBeforeUpdate);
        ContactInformation testContactInformation = contactInformationList.get(contactInformationList.size() - 1);
        assertThat(testContactInformation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testContactInformation.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testContactInformation.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void patchNonExistingContactInformation() throws Exception {
        int databaseSizeBeforeUpdate = contactInformationRepository.findAll().size();
        contactInformation.setId(count.incrementAndGet());

        // Create the ContactInformation
        ContactInformationDTO contactInformationDTO = contactInformationMapper.toDto(contactInformation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contactInformationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactInformationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactInformation in the database
        List<ContactInformation> contactInformationList = contactInformationRepository.findAll();
        assertThat(contactInformationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContactInformation() throws Exception {
        int databaseSizeBeforeUpdate = contactInformationRepository.findAll().size();
        contactInformation.setId(count.incrementAndGet());

        // Create the ContactInformation
        ContactInformationDTO contactInformationDTO = contactInformationMapper.toDto(contactInformation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactInformationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactInformation in the database
        List<ContactInformation> contactInformationList = contactInformationRepository.findAll();
        assertThat(contactInformationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContactInformation() throws Exception {
        int databaseSizeBeforeUpdate = contactInformationRepository.findAll().size();
        contactInformation.setId(count.incrementAndGet());

        // Create the ContactInformation
        ContactInformationDTO contactInformationDTO = contactInformationMapper.toDto(contactInformation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactInformationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactInformationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactInformation in the database
        List<ContactInformation> contactInformationList = contactInformationRepository.findAll();
        assertThat(contactInformationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContactInformation() throws Exception {
        // Initialize the database
        contactInformationRepository.saveAndFlush(contactInformation);

        int databaseSizeBeforeDelete = contactInformationRepository.findAll().size();

        // Delete the contactInformation
        restContactInformationMockMvc
            .perform(delete(ENTITY_API_URL_ID, contactInformation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContactInformation> contactInformationList = contactInformationRepository.findAll();
        assertThat(contactInformationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
