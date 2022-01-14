package com.lost.child.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.lost.child.IntegrationTest;
import com.lost.child.domain.LastSeen;
import com.lost.child.repository.LastSeenRepository;
import com.lost.child.service.dto.LastSeenDTO;
import com.lost.child.service.mapper.LastSeenMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link LastSeenResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LastSeenResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/last-seens";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LastSeenRepository lastSeenRepository;

    @Autowired
    private LastSeenMapper lastSeenMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLastSeenMockMvc;

    private LastSeen lastSeen;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LastSeen createEntity(EntityManager em) {
        LastSeen lastSeen = new LastSeen().date(DEFAULT_DATE);
        return lastSeen;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LastSeen createUpdatedEntity(EntityManager em) {
        LastSeen lastSeen = new LastSeen().date(UPDATED_DATE);
        return lastSeen;
    }

    @BeforeEach
    public void initTest() {
        lastSeen = createEntity(em);
    }

    @Test
    @Transactional
    void createLastSeen() throws Exception {
        int databaseSizeBeforeCreate = lastSeenRepository.findAll().size();
        // Create the LastSeen
        LastSeenDTO lastSeenDTO = lastSeenMapper.toDto(lastSeen);
        restLastSeenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lastSeenDTO)))
            .andExpect(status().isCreated());

        // Validate the LastSeen in the database
        List<LastSeen> lastSeenList = lastSeenRepository.findAll();
        assertThat(lastSeenList).hasSize(databaseSizeBeforeCreate + 1);
        LastSeen testLastSeen = lastSeenList.get(lastSeenList.size() - 1);
        assertThat(testLastSeen.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createLastSeenWithExistingId() throws Exception {
        // Create the LastSeen with an existing ID
        lastSeen.setId(1L);
        LastSeenDTO lastSeenDTO = lastSeenMapper.toDto(lastSeen);

        int databaseSizeBeforeCreate = lastSeenRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLastSeenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lastSeenDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LastSeen in the database
        List<LastSeen> lastSeenList = lastSeenRepository.findAll();
        assertThat(lastSeenList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = lastSeenRepository.findAll().size();
        // set the field null
        lastSeen.setDate(null);

        // Create the LastSeen, which fails.
        LastSeenDTO lastSeenDTO = lastSeenMapper.toDto(lastSeen);

        restLastSeenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lastSeenDTO)))
            .andExpect(status().isBadRequest());

        List<LastSeen> lastSeenList = lastSeenRepository.findAll();
        assertThat(lastSeenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLastSeens() throws Exception {
        // Initialize the database
        lastSeenRepository.saveAndFlush(lastSeen);

        // Get all the lastSeenList
        restLastSeenMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lastSeen.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    void getLastSeen() throws Exception {
        // Initialize the database
        lastSeenRepository.saveAndFlush(lastSeen);

        // Get the lastSeen
        restLastSeenMockMvc
            .perform(get(ENTITY_API_URL_ID, lastSeen.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lastSeen.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingLastSeen() throws Exception {
        // Get the lastSeen
        restLastSeenMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLastSeen() throws Exception {
        // Initialize the database
        lastSeenRepository.saveAndFlush(lastSeen);

        int databaseSizeBeforeUpdate = lastSeenRepository.findAll().size();

        // Update the lastSeen
        LastSeen updatedLastSeen = lastSeenRepository.findById(lastSeen.getId()).get();
        // Disconnect from session so that the updates on updatedLastSeen are not directly saved in db
        em.detach(updatedLastSeen);
        updatedLastSeen.date(UPDATED_DATE);
        LastSeenDTO lastSeenDTO = lastSeenMapper.toDto(updatedLastSeen);

        restLastSeenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lastSeenDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lastSeenDTO))
            )
            .andExpect(status().isOk());

        // Validate the LastSeen in the database
        List<LastSeen> lastSeenList = lastSeenRepository.findAll();
        assertThat(lastSeenList).hasSize(databaseSizeBeforeUpdate);
        LastSeen testLastSeen = lastSeenList.get(lastSeenList.size() - 1);
        assertThat(testLastSeen.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingLastSeen() throws Exception {
        int databaseSizeBeforeUpdate = lastSeenRepository.findAll().size();
        lastSeen.setId(count.incrementAndGet());

        // Create the LastSeen
        LastSeenDTO lastSeenDTO = lastSeenMapper.toDto(lastSeen);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLastSeenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lastSeenDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lastSeenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LastSeen in the database
        List<LastSeen> lastSeenList = lastSeenRepository.findAll();
        assertThat(lastSeenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLastSeen() throws Exception {
        int databaseSizeBeforeUpdate = lastSeenRepository.findAll().size();
        lastSeen.setId(count.incrementAndGet());

        // Create the LastSeen
        LastSeenDTO lastSeenDTO = lastSeenMapper.toDto(lastSeen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLastSeenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lastSeenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LastSeen in the database
        List<LastSeen> lastSeenList = lastSeenRepository.findAll();
        assertThat(lastSeenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLastSeen() throws Exception {
        int databaseSizeBeforeUpdate = lastSeenRepository.findAll().size();
        lastSeen.setId(count.incrementAndGet());

        // Create the LastSeen
        LastSeenDTO lastSeenDTO = lastSeenMapper.toDto(lastSeen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLastSeenMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lastSeenDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LastSeen in the database
        List<LastSeen> lastSeenList = lastSeenRepository.findAll();
        assertThat(lastSeenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLastSeenWithPatch() throws Exception {
        // Initialize the database
        lastSeenRepository.saveAndFlush(lastSeen);

        int databaseSizeBeforeUpdate = lastSeenRepository.findAll().size();

        // Update the lastSeen using partial update
        LastSeen partialUpdatedLastSeen = new LastSeen();
        partialUpdatedLastSeen.setId(lastSeen.getId());

        partialUpdatedLastSeen.date(UPDATED_DATE);

        restLastSeenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLastSeen.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLastSeen))
            )
            .andExpect(status().isOk());

        // Validate the LastSeen in the database
        List<LastSeen> lastSeenList = lastSeenRepository.findAll();
        assertThat(lastSeenList).hasSize(databaseSizeBeforeUpdate);
        LastSeen testLastSeen = lastSeenList.get(lastSeenList.size() - 1);
        assertThat(testLastSeen.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateLastSeenWithPatch() throws Exception {
        // Initialize the database
        lastSeenRepository.saveAndFlush(lastSeen);

        int databaseSizeBeforeUpdate = lastSeenRepository.findAll().size();

        // Update the lastSeen using partial update
        LastSeen partialUpdatedLastSeen = new LastSeen();
        partialUpdatedLastSeen.setId(lastSeen.getId());

        partialUpdatedLastSeen.date(UPDATED_DATE);

        restLastSeenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLastSeen.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLastSeen))
            )
            .andExpect(status().isOk());

        // Validate the LastSeen in the database
        List<LastSeen> lastSeenList = lastSeenRepository.findAll();
        assertThat(lastSeenList).hasSize(databaseSizeBeforeUpdate);
        LastSeen testLastSeen = lastSeenList.get(lastSeenList.size() - 1);
        assertThat(testLastSeen.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingLastSeen() throws Exception {
        int databaseSizeBeforeUpdate = lastSeenRepository.findAll().size();
        lastSeen.setId(count.incrementAndGet());

        // Create the LastSeen
        LastSeenDTO lastSeenDTO = lastSeenMapper.toDto(lastSeen);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLastSeenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lastSeenDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lastSeenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LastSeen in the database
        List<LastSeen> lastSeenList = lastSeenRepository.findAll();
        assertThat(lastSeenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLastSeen() throws Exception {
        int databaseSizeBeforeUpdate = lastSeenRepository.findAll().size();
        lastSeen.setId(count.incrementAndGet());

        // Create the LastSeen
        LastSeenDTO lastSeenDTO = lastSeenMapper.toDto(lastSeen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLastSeenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lastSeenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LastSeen in the database
        List<LastSeen> lastSeenList = lastSeenRepository.findAll();
        assertThat(lastSeenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLastSeen() throws Exception {
        int databaseSizeBeforeUpdate = lastSeenRepository.findAll().size();
        lastSeen.setId(count.incrementAndGet());

        // Create the LastSeen
        LastSeenDTO lastSeenDTO = lastSeenMapper.toDto(lastSeen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLastSeenMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(lastSeenDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LastSeen in the database
        List<LastSeen> lastSeenList = lastSeenRepository.findAll();
        assertThat(lastSeenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLastSeen() throws Exception {
        // Initialize the database
        lastSeenRepository.saveAndFlush(lastSeen);

        int databaseSizeBeforeDelete = lastSeenRepository.findAll().size();

        // Delete the lastSeen
        restLastSeenMockMvc
            .perform(delete(ENTITY_API_URL_ID, lastSeen.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LastSeen> lastSeenList = lastSeenRepository.findAll();
        assertThat(lastSeenList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
