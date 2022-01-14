package com.lost.child.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.lost.child.IntegrationTest;
import com.lost.child.domain.Description;
import com.lost.child.repository.DescriptionRepository;
import com.lost.child.service.dto.DescriptionDTO;
import com.lost.child.service.mapper.DescriptionMapper;
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
 * Integration tests for the {@link DescriptionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DescriptionResourceIT {

    private static final String DEFAULT_EYE_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_EYE_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_HAIR_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_HAIR_COLOR = "BBBBBBBBBB";

    private static final Integer DEFAULT_WEIGHT = 1;
    private static final Integer UPDATED_WEIGHT = 2;

    private static final Integer DEFAULT_HEIGHT = 1;
    private static final Integer UPDATED_HEIGHT = 2;

    private static final String ENTITY_API_URL = "/api/descriptions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DescriptionRepository descriptionRepository;

    @Autowired
    private DescriptionMapper descriptionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDescriptionMockMvc;

    private Description description;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Description createEntity(EntityManager em) {
        Description description = new Description()
            .eyeColor(DEFAULT_EYE_COLOR)
            .hairColor(DEFAULT_HAIR_COLOR)
            .weight(DEFAULT_WEIGHT)
            .height(DEFAULT_HEIGHT);
        return description;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Description createUpdatedEntity(EntityManager em) {
        Description description = new Description()
            .eyeColor(UPDATED_EYE_COLOR)
            .hairColor(UPDATED_HAIR_COLOR)
            .weight(UPDATED_WEIGHT)
            .height(UPDATED_HEIGHT);
        return description;
    }

    @BeforeEach
    public void initTest() {
        description = createEntity(em);
    }

    @Test
    @Transactional
    void createDescription() throws Exception {
        int databaseSizeBeforeCreate = descriptionRepository.findAll().size();
        // Create the Description
        DescriptionDTO descriptionDTO = descriptionMapper.toDto(description);
        restDescriptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(descriptionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Description in the database
        List<Description> descriptionList = descriptionRepository.findAll();
        assertThat(descriptionList).hasSize(databaseSizeBeforeCreate + 1);
        Description testDescription = descriptionList.get(descriptionList.size() - 1);
        assertThat(testDescription.getEyeColor()).isEqualTo(DEFAULT_EYE_COLOR);
        assertThat(testDescription.getHairColor()).isEqualTo(DEFAULT_HAIR_COLOR);
        assertThat(testDescription.getWeight()).isEqualTo(DEFAULT_WEIGHT);
        assertThat(testDescription.getHeight()).isEqualTo(DEFAULT_HEIGHT);
    }

    @Test
    @Transactional
    void createDescriptionWithExistingId() throws Exception {
        // Create the Description with an existing ID
        description.setId(1L);
        DescriptionDTO descriptionDTO = descriptionMapper.toDto(description);

        int databaseSizeBeforeCreate = descriptionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDescriptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(descriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Description in the database
        List<Description> descriptionList = descriptionRepository.findAll();
        assertThat(descriptionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEyeColorIsRequired() throws Exception {
        int databaseSizeBeforeTest = descriptionRepository.findAll().size();
        // set the field null
        description.setEyeColor(null);

        // Create the Description, which fails.
        DescriptionDTO descriptionDTO = descriptionMapper.toDto(description);

        restDescriptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(descriptionDTO))
            )
            .andExpect(status().isBadRequest());

        List<Description> descriptionList = descriptionRepository.findAll();
        assertThat(descriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHairColorIsRequired() throws Exception {
        int databaseSizeBeforeTest = descriptionRepository.findAll().size();
        // set the field null
        description.setHairColor(null);

        // Create the Description, which fails.
        DescriptionDTO descriptionDTO = descriptionMapper.toDto(description);

        restDescriptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(descriptionDTO))
            )
            .andExpect(status().isBadRequest());

        List<Description> descriptionList = descriptionRepository.findAll();
        assertThat(descriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWeightIsRequired() throws Exception {
        int databaseSizeBeforeTest = descriptionRepository.findAll().size();
        // set the field null
        description.setWeight(null);

        // Create the Description, which fails.
        DescriptionDTO descriptionDTO = descriptionMapper.toDto(description);

        restDescriptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(descriptionDTO))
            )
            .andExpect(status().isBadRequest());

        List<Description> descriptionList = descriptionRepository.findAll();
        assertThat(descriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHeightIsRequired() throws Exception {
        int databaseSizeBeforeTest = descriptionRepository.findAll().size();
        // set the field null
        description.setHeight(null);

        // Create the Description, which fails.
        DescriptionDTO descriptionDTO = descriptionMapper.toDto(description);

        restDescriptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(descriptionDTO))
            )
            .andExpect(status().isBadRequest());

        List<Description> descriptionList = descriptionRepository.findAll();
        assertThat(descriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDescriptions() throws Exception {
        // Initialize the database
        descriptionRepository.saveAndFlush(description);

        // Get all the descriptionList
        restDescriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(description.getId().intValue())))
            .andExpect(jsonPath("$.[*].eyeColor").value(hasItem(DEFAULT_EYE_COLOR)))
            .andExpect(jsonPath("$.[*].hairColor").value(hasItem(DEFAULT_HAIR_COLOR)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT)))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT)));
    }

    @Test
    @Transactional
    void getDescription() throws Exception {
        // Initialize the database
        descriptionRepository.saveAndFlush(description);

        // Get the description
        restDescriptionMockMvc
            .perform(get(ENTITY_API_URL_ID, description.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(description.getId().intValue()))
            .andExpect(jsonPath("$.eyeColor").value(DEFAULT_EYE_COLOR))
            .andExpect(jsonPath("$.hairColor").value(DEFAULT_HAIR_COLOR))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT))
            .andExpect(jsonPath("$.height").value(DEFAULT_HEIGHT));
    }

    @Test
    @Transactional
    void getNonExistingDescription() throws Exception {
        // Get the description
        restDescriptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDescription() throws Exception {
        // Initialize the database
        descriptionRepository.saveAndFlush(description);

        int databaseSizeBeforeUpdate = descriptionRepository.findAll().size();

        // Update the description
        Description updatedDescription = descriptionRepository.findById(description.getId()).get();
        // Disconnect from session so that the updates on updatedDescription are not directly saved in db
        em.detach(updatedDescription);
        updatedDescription.eyeColor(UPDATED_EYE_COLOR).hairColor(UPDATED_HAIR_COLOR).weight(UPDATED_WEIGHT).height(UPDATED_HEIGHT);
        DescriptionDTO descriptionDTO = descriptionMapper.toDto(updatedDescription);

        restDescriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, descriptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(descriptionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Description in the database
        List<Description> descriptionList = descriptionRepository.findAll();
        assertThat(descriptionList).hasSize(databaseSizeBeforeUpdate);
        Description testDescription = descriptionList.get(descriptionList.size() - 1);
        assertThat(testDescription.getEyeColor()).isEqualTo(UPDATED_EYE_COLOR);
        assertThat(testDescription.getHairColor()).isEqualTo(UPDATED_HAIR_COLOR);
        assertThat(testDescription.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testDescription.getHeight()).isEqualTo(UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void putNonExistingDescription() throws Exception {
        int databaseSizeBeforeUpdate = descriptionRepository.findAll().size();
        description.setId(count.incrementAndGet());

        // Create the Description
        DescriptionDTO descriptionDTO = descriptionMapper.toDto(description);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDescriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, descriptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(descriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Description in the database
        List<Description> descriptionList = descriptionRepository.findAll();
        assertThat(descriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDescription() throws Exception {
        int databaseSizeBeforeUpdate = descriptionRepository.findAll().size();
        description.setId(count.incrementAndGet());

        // Create the Description
        DescriptionDTO descriptionDTO = descriptionMapper.toDto(description);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDescriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(descriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Description in the database
        List<Description> descriptionList = descriptionRepository.findAll();
        assertThat(descriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDescription() throws Exception {
        int databaseSizeBeforeUpdate = descriptionRepository.findAll().size();
        description.setId(count.incrementAndGet());

        // Create the Description
        DescriptionDTO descriptionDTO = descriptionMapper.toDto(description);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDescriptionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(descriptionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Description in the database
        List<Description> descriptionList = descriptionRepository.findAll();
        assertThat(descriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDescriptionWithPatch() throws Exception {
        // Initialize the database
        descriptionRepository.saveAndFlush(description);

        int databaseSizeBeforeUpdate = descriptionRepository.findAll().size();

        // Update the description using partial update
        Description partialUpdatedDescription = new Description();
        partialUpdatedDescription.setId(description.getId());

        partialUpdatedDescription.eyeColor(UPDATED_EYE_COLOR).hairColor(UPDATED_HAIR_COLOR).weight(UPDATED_WEIGHT);

        restDescriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDescription.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDescription))
            )
            .andExpect(status().isOk());

        // Validate the Description in the database
        List<Description> descriptionList = descriptionRepository.findAll();
        assertThat(descriptionList).hasSize(databaseSizeBeforeUpdate);
        Description testDescription = descriptionList.get(descriptionList.size() - 1);
        assertThat(testDescription.getEyeColor()).isEqualTo(UPDATED_EYE_COLOR);
        assertThat(testDescription.getHairColor()).isEqualTo(UPDATED_HAIR_COLOR);
        assertThat(testDescription.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testDescription.getHeight()).isEqualTo(DEFAULT_HEIGHT);
    }

    @Test
    @Transactional
    void fullUpdateDescriptionWithPatch() throws Exception {
        // Initialize the database
        descriptionRepository.saveAndFlush(description);

        int databaseSizeBeforeUpdate = descriptionRepository.findAll().size();

        // Update the description using partial update
        Description partialUpdatedDescription = new Description();
        partialUpdatedDescription.setId(description.getId());

        partialUpdatedDescription.eyeColor(UPDATED_EYE_COLOR).hairColor(UPDATED_HAIR_COLOR).weight(UPDATED_WEIGHT).height(UPDATED_HEIGHT);

        restDescriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDescription.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDescription))
            )
            .andExpect(status().isOk());

        // Validate the Description in the database
        List<Description> descriptionList = descriptionRepository.findAll();
        assertThat(descriptionList).hasSize(databaseSizeBeforeUpdate);
        Description testDescription = descriptionList.get(descriptionList.size() - 1);
        assertThat(testDescription.getEyeColor()).isEqualTo(UPDATED_EYE_COLOR);
        assertThat(testDescription.getHairColor()).isEqualTo(UPDATED_HAIR_COLOR);
        assertThat(testDescription.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testDescription.getHeight()).isEqualTo(UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void patchNonExistingDescription() throws Exception {
        int databaseSizeBeforeUpdate = descriptionRepository.findAll().size();
        description.setId(count.incrementAndGet());

        // Create the Description
        DescriptionDTO descriptionDTO = descriptionMapper.toDto(description);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDescriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, descriptionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(descriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Description in the database
        List<Description> descriptionList = descriptionRepository.findAll();
        assertThat(descriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDescription() throws Exception {
        int databaseSizeBeforeUpdate = descriptionRepository.findAll().size();
        description.setId(count.incrementAndGet());

        // Create the Description
        DescriptionDTO descriptionDTO = descriptionMapper.toDto(description);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDescriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(descriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Description in the database
        List<Description> descriptionList = descriptionRepository.findAll();
        assertThat(descriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDescription() throws Exception {
        int databaseSizeBeforeUpdate = descriptionRepository.findAll().size();
        description.setId(count.incrementAndGet());

        // Create the Description
        DescriptionDTO descriptionDTO = descriptionMapper.toDto(description);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDescriptionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(descriptionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Description in the database
        List<Description> descriptionList = descriptionRepository.findAll();
        assertThat(descriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDescription() throws Exception {
        // Initialize the database
        descriptionRepository.saveAndFlush(description);

        int databaseSizeBeforeDelete = descriptionRepository.findAll().size();

        // Delete the description
        restDescriptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, description.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Description> descriptionList = descriptionRepository.findAll();
        assertThat(descriptionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
