package com.lost.child.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.lost.child.IntegrationTest;
import com.lost.child.domain.LastSeenAddress;
import com.lost.child.repository.LastSeenAddressRepository;
import com.lost.child.service.dto.LastSeenAddressDTO;
import com.lost.child.service.mapper.LastSeenAddressMapper;
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
 * Integration tests for the {@link LastSeenAddressResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LastSeenAddressResourceIT {

    private static final String DEFAULT_STREET = "AAAAAAAAAA";
    private static final String UPDATED_STREET = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/last-seen-addresses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LastSeenAddressRepository lastSeenAddressRepository;

    @Autowired
    private LastSeenAddressMapper lastSeenAddressMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLastSeenAddressMockMvc;

    private LastSeenAddress lastSeenAddress;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LastSeenAddress createEntity(EntityManager em) {
        LastSeenAddress lastSeenAddress = new LastSeenAddress()
            .street(DEFAULT_STREET)
            .city(DEFAULT_CITY)
            .state(DEFAULT_STATE)
            .country(DEFAULT_COUNTRY);
        return lastSeenAddress;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LastSeenAddress createUpdatedEntity(EntityManager em) {
        LastSeenAddress lastSeenAddress = new LastSeenAddress()
            .street(UPDATED_STREET)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .country(UPDATED_COUNTRY);
        return lastSeenAddress;
    }

    @BeforeEach
    public void initTest() {
        lastSeenAddress = createEntity(em);
    }

    @Test
    @Transactional
    void createLastSeenAddress() throws Exception {
        int databaseSizeBeforeCreate = lastSeenAddressRepository.findAll().size();
        // Create the LastSeenAddress
        LastSeenAddressDTO lastSeenAddressDTO = lastSeenAddressMapper.toDto(lastSeenAddress);
        restLastSeenAddressMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lastSeenAddressDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LastSeenAddress in the database
        List<LastSeenAddress> lastSeenAddressList = lastSeenAddressRepository.findAll();
        assertThat(lastSeenAddressList).hasSize(databaseSizeBeforeCreate + 1);
        LastSeenAddress testLastSeenAddress = lastSeenAddressList.get(lastSeenAddressList.size() - 1);
        assertThat(testLastSeenAddress.getStreet()).isEqualTo(DEFAULT_STREET);
        assertThat(testLastSeenAddress.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testLastSeenAddress.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testLastSeenAddress.getCountry()).isEqualTo(DEFAULT_COUNTRY);
    }

    @Test
    @Transactional
    void createLastSeenAddressWithExistingId() throws Exception {
        // Create the LastSeenAddress with an existing ID
        lastSeenAddress.setId(1L);
        LastSeenAddressDTO lastSeenAddressDTO = lastSeenAddressMapper.toDto(lastSeenAddress);

        int databaseSizeBeforeCreate = lastSeenAddressRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLastSeenAddressMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lastSeenAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LastSeenAddress in the database
        List<LastSeenAddress> lastSeenAddressList = lastSeenAddressRepository.findAll();
        assertThat(lastSeenAddressList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStreetIsRequired() throws Exception {
        int databaseSizeBeforeTest = lastSeenAddressRepository.findAll().size();
        // set the field null
        lastSeenAddress.setStreet(null);

        // Create the LastSeenAddress, which fails.
        LastSeenAddressDTO lastSeenAddressDTO = lastSeenAddressMapper.toDto(lastSeenAddress);

        restLastSeenAddressMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lastSeenAddressDTO))
            )
            .andExpect(status().isBadRequest());

        List<LastSeenAddress> lastSeenAddressList = lastSeenAddressRepository.findAll();
        assertThat(lastSeenAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = lastSeenAddressRepository.findAll().size();
        // set the field null
        lastSeenAddress.setCity(null);

        // Create the LastSeenAddress, which fails.
        LastSeenAddressDTO lastSeenAddressDTO = lastSeenAddressMapper.toDto(lastSeenAddress);

        restLastSeenAddressMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lastSeenAddressDTO))
            )
            .andExpect(status().isBadRequest());

        List<LastSeenAddress> lastSeenAddressList = lastSeenAddressRepository.findAll();
        assertThat(lastSeenAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLastSeenAddresses() throws Exception {
        // Initialize the database
        lastSeenAddressRepository.saveAndFlush(lastSeenAddress);

        // Get all the lastSeenAddressList
        restLastSeenAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lastSeenAddress.getId().intValue())))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)));
    }

    @Test
    @Transactional
    void getLastSeenAddress() throws Exception {
        // Initialize the database
        lastSeenAddressRepository.saveAndFlush(lastSeenAddress);

        // Get the lastSeenAddress
        restLastSeenAddressMockMvc
            .perform(get(ENTITY_API_URL_ID, lastSeenAddress.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lastSeenAddress.getId().intValue()))
            .andExpect(jsonPath("$.street").value(DEFAULT_STREET))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY));
    }

    @Test
    @Transactional
    void getNonExistingLastSeenAddress() throws Exception {
        // Get the lastSeenAddress
        restLastSeenAddressMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLastSeenAddress() throws Exception {
        // Initialize the database
        lastSeenAddressRepository.saveAndFlush(lastSeenAddress);

        int databaseSizeBeforeUpdate = lastSeenAddressRepository.findAll().size();

        // Update the lastSeenAddress
        LastSeenAddress updatedLastSeenAddress = lastSeenAddressRepository.findById(lastSeenAddress.getId()).get();
        // Disconnect from session so that the updates on updatedLastSeenAddress are not directly saved in db
        em.detach(updatedLastSeenAddress);
        updatedLastSeenAddress.street(UPDATED_STREET).city(UPDATED_CITY).state(UPDATED_STATE).country(UPDATED_COUNTRY);
        LastSeenAddressDTO lastSeenAddressDTO = lastSeenAddressMapper.toDto(updatedLastSeenAddress);

        restLastSeenAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lastSeenAddressDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lastSeenAddressDTO))
            )
            .andExpect(status().isOk());

        // Validate the LastSeenAddress in the database
        List<LastSeenAddress> lastSeenAddressList = lastSeenAddressRepository.findAll();
        assertThat(lastSeenAddressList).hasSize(databaseSizeBeforeUpdate);
        LastSeenAddress testLastSeenAddress = lastSeenAddressList.get(lastSeenAddressList.size() - 1);
        assertThat(testLastSeenAddress.getStreet()).isEqualTo(UPDATED_STREET);
        assertThat(testLastSeenAddress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testLastSeenAddress.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testLastSeenAddress.getCountry()).isEqualTo(UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void putNonExistingLastSeenAddress() throws Exception {
        int databaseSizeBeforeUpdate = lastSeenAddressRepository.findAll().size();
        lastSeenAddress.setId(count.incrementAndGet());

        // Create the LastSeenAddress
        LastSeenAddressDTO lastSeenAddressDTO = lastSeenAddressMapper.toDto(lastSeenAddress);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLastSeenAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lastSeenAddressDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lastSeenAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LastSeenAddress in the database
        List<LastSeenAddress> lastSeenAddressList = lastSeenAddressRepository.findAll();
        assertThat(lastSeenAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLastSeenAddress() throws Exception {
        int databaseSizeBeforeUpdate = lastSeenAddressRepository.findAll().size();
        lastSeenAddress.setId(count.incrementAndGet());

        // Create the LastSeenAddress
        LastSeenAddressDTO lastSeenAddressDTO = lastSeenAddressMapper.toDto(lastSeenAddress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLastSeenAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lastSeenAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LastSeenAddress in the database
        List<LastSeenAddress> lastSeenAddressList = lastSeenAddressRepository.findAll();
        assertThat(lastSeenAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLastSeenAddress() throws Exception {
        int databaseSizeBeforeUpdate = lastSeenAddressRepository.findAll().size();
        lastSeenAddress.setId(count.incrementAndGet());

        // Create the LastSeenAddress
        LastSeenAddressDTO lastSeenAddressDTO = lastSeenAddressMapper.toDto(lastSeenAddress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLastSeenAddressMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lastSeenAddressDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LastSeenAddress in the database
        List<LastSeenAddress> lastSeenAddressList = lastSeenAddressRepository.findAll();
        assertThat(lastSeenAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLastSeenAddressWithPatch() throws Exception {
        // Initialize the database
        lastSeenAddressRepository.saveAndFlush(lastSeenAddress);

        int databaseSizeBeforeUpdate = lastSeenAddressRepository.findAll().size();

        // Update the lastSeenAddress using partial update
        LastSeenAddress partialUpdatedLastSeenAddress = new LastSeenAddress();
        partialUpdatedLastSeenAddress.setId(lastSeenAddress.getId());

        partialUpdatedLastSeenAddress.city(UPDATED_CITY).state(UPDATED_STATE);

        restLastSeenAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLastSeenAddress.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLastSeenAddress))
            )
            .andExpect(status().isOk());

        // Validate the LastSeenAddress in the database
        List<LastSeenAddress> lastSeenAddressList = lastSeenAddressRepository.findAll();
        assertThat(lastSeenAddressList).hasSize(databaseSizeBeforeUpdate);
        LastSeenAddress testLastSeenAddress = lastSeenAddressList.get(lastSeenAddressList.size() - 1);
        assertThat(testLastSeenAddress.getStreet()).isEqualTo(DEFAULT_STREET);
        assertThat(testLastSeenAddress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testLastSeenAddress.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testLastSeenAddress.getCountry()).isEqualTo(DEFAULT_COUNTRY);
    }

    @Test
    @Transactional
    void fullUpdateLastSeenAddressWithPatch() throws Exception {
        // Initialize the database
        lastSeenAddressRepository.saveAndFlush(lastSeenAddress);

        int databaseSizeBeforeUpdate = lastSeenAddressRepository.findAll().size();

        // Update the lastSeenAddress using partial update
        LastSeenAddress partialUpdatedLastSeenAddress = new LastSeenAddress();
        partialUpdatedLastSeenAddress.setId(lastSeenAddress.getId());

        partialUpdatedLastSeenAddress.street(UPDATED_STREET).city(UPDATED_CITY).state(UPDATED_STATE).country(UPDATED_COUNTRY);

        restLastSeenAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLastSeenAddress.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLastSeenAddress))
            )
            .andExpect(status().isOk());

        // Validate the LastSeenAddress in the database
        List<LastSeenAddress> lastSeenAddressList = lastSeenAddressRepository.findAll();
        assertThat(lastSeenAddressList).hasSize(databaseSizeBeforeUpdate);
        LastSeenAddress testLastSeenAddress = lastSeenAddressList.get(lastSeenAddressList.size() - 1);
        assertThat(testLastSeenAddress.getStreet()).isEqualTo(UPDATED_STREET);
        assertThat(testLastSeenAddress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testLastSeenAddress.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testLastSeenAddress.getCountry()).isEqualTo(UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void patchNonExistingLastSeenAddress() throws Exception {
        int databaseSizeBeforeUpdate = lastSeenAddressRepository.findAll().size();
        lastSeenAddress.setId(count.incrementAndGet());

        // Create the LastSeenAddress
        LastSeenAddressDTO lastSeenAddressDTO = lastSeenAddressMapper.toDto(lastSeenAddress);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLastSeenAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lastSeenAddressDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lastSeenAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LastSeenAddress in the database
        List<LastSeenAddress> lastSeenAddressList = lastSeenAddressRepository.findAll();
        assertThat(lastSeenAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLastSeenAddress() throws Exception {
        int databaseSizeBeforeUpdate = lastSeenAddressRepository.findAll().size();
        lastSeenAddress.setId(count.incrementAndGet());

        // Create the LastSeenAddress
        LastSeenAddressDTO lastSeenAddressDTO = lastSeenAddressMapper.toDto(lastSeenAddress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLastSeenAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lastSeenAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LastSeenAddress in the database
        List<LastSeenAddress> lastSeenAddressList = lastSeenAddressRepository.findAll();
        assertThat(lastSeenAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLastSeenAddress() throws Exception {
        int databaseSizeBeforeUpdate = lastSeenAddressRepository.findAll().size();
        lastSeenAddress.setId(count.incrementAndGet());

        // Create the LastSeenAddress
        LastSeenAddressDTO lastSeenAddressDTO = lastSeenAddressMapper.toDto(lastSeenAddress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLastSeenAddressMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lastSeenAddressDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LastSeenAddress in the database
        List<LastSeenAddress> lastSeenAddressList = lastSeenAddressRepository.findAll();
        assertThat(lastSeenAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLastSeenAddress() throws Exception {
        // Initialize the database
        lastSeenAddressRepository.saveAndFlush(lastSeenAddress);

        int databaseSizeBeforeDelete = lastSeenAddressRepository.findAll().size();

        // Delete the lastSeenAddress
        restLastSeenAddressMockMvc
            .perform(delete(ENTITY_API_URL_ID, lastSeenAddress.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LastSeenAddress> lastSeenAddressList = lastSeenAddressRepository.findAll();
        assertThat(lastSeenAddressList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
