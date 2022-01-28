package com.lost.child.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.lost.child.IntegrationTest;
import com.lost.child.domain.Child;
import com.lost.child.domain.enumeration.Gender;
import com.lost.child.domain.enumeration.Status;
import com.lost.child.repository.ChildRepository;
import com.lost.child.service.dto.ChildDTO;
import com.lost.child.service.mapper.ChildMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ChildResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ChildResourceIT {

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_AGE = 0;
    private static final Integer UPDATED_AGE = 1;

    private static final Gender DEFAULT_GENDER = Gender.Male;
    private static final Gender UPDATED_GENDER = Gender.Female;

    private static final LocalDate DEFAULT_BIRTHDATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTHDATE = LocalDate.now(ZoneId.systemDefault());

    private static final Status DEFAULT_STATUS = Status.Open;
    private static final Status UPDATED_STATUS = Status.Close;

    private static final String DEFAULT_AGENCY = "AAAAA";
    private static final String UPDATED_AGENCY = "BBBBB";

    private static final String ENTITY_API_URL = "/api/children";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ChildRepository childRepository;

    @Autowired
    private ChildMapper childMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restChildMockMvc;

    private Child child;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Child createEntity(EntityManager em) {
        Child child = new Child()
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .name(DEFAULT_NAME)
            .age(DEFAULT_AGE)
            .gender(DEFAULT_GENDER)
            .birthdate(DEFAULT_BIRTHDATE)
            .status(DEFAULT_STATUS)
            .agency(DEFAULT_AGENCY);
        return child;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Child createUpdatedEntity(EntityManager em) {
        Child child = new Child()
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .name(UPDATED_NAME)
            .age(UPDATED_AGE)
            .gender(UPDATED_GENDER)
            .birthdate(UPDATED_BIRTHDATE)
            .status(UPDATED_STATUS)
            .agency(UPDATED_AGENCY);
        return child;
    }

    @BeforeEach
    public void initTest() {
        child = createEntity(em);
    }

    @Test
    @Transactional
    void createChild() throws Exception {
        int databaseSizeBeforeCreate = childRepository.findAll().size();
        // Create the Child
        ChildDTO childDTO = childMapper.toDto(child);
        restChildMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(childDTO)))
            .andExpect(status().isCreated());

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeCreate + 1);
        Child testChild = childList.get(childList.size() - 1);
        assertThat(testChild.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testChild.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testChild.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testChild.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testChild.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testChild.getBirthdate()).isEqualTo(DEFAULT_BIRTHDATE);
        assertThat(testChild.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testChild.getAgency()).isEqualTo(DEFAULT_AGENCY);
    }

    @Test
    @Transactional
    void createChildWithExistingId() throws Exception {
        // Create the Child with an existing ID
        child.setId(1L);
        ChildDTO childDTO = childMapper.toDto(child);

        int databaseSizeBeforeCreate = childRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restChildMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(childDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = childRepository.findAll().size();
        // set the field null
        child.setName(null);

        // Create the Child, which fails.
        ChildDTO childDTO = childMapper.toDto(child);

        restChildMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(childDTO)))
            .andExpect(status().isBadRequest());

        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAgeIsRequired() throws Exception {
        int databaseSizeBeforeTest = childRepository.findAll().size();
        // set the field null
        child.setAge(null);

        // Create the Child, which fails.
        ChildDTO childDTO = childMapper.toDto(child);

        restChildMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(childDTO)))
            .andExpect(status().isBadRequest());

        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGenderIsRequired() throws Exception {
        int databaseSizeBeforeTest = childRepository.findAll().size();
        // set the field null
        child.setGender(null);

        // Create the Child, which fails.
        ChildDTO childDTO = childMapper.toDto(child);

        restChildMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(childDTO)))
            .andExpect(status().isBadRequest());

        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBirthdateIsRequired() throws Exception {
        int databaseSizeBeforeTest = childRepository.findAll().size();
        // set the field null
        child.setBirthdate(null);

        // Create the Child, which fails.
        ChildDTO childDTO = childMapper.toDto(child);

        restChildMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(childDTO)))
            .andExpect(status().isBadRequest());

        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = childRepository.findAll().size();
        // set the field null
        child.setStatus(null);

        // Create the Child, which fails.
        ChildDTO childDTO = childMapper.toDto(child);

        restChildMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(childDTO)))
            .andExpect(status().isBadRequest());

        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllChildren() throws Exception {
        // Initialize the database
        childRepository.saveAndFlush(child);

        // Get all the childList
        restChildMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(child.getId().intValue())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].birthdate").value(hasItem(DEFAULT_BIRTHDATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].agency").value(hasItem(DEFAULT_AGENCY)));
    }

    @Test
    @Transactional
    void getChild() throws Exception {
        // Initialize the database
        childRepository.saveAndFlush(child);

        // Get the child
        restChildMockMvc
            .perform(get(ENTITY_API_URL_ID, child.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(child.getId().intValue()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.birthdate").value(DEFAULT_BIRTHDATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.agency").value(DEFAULT_AGENCY));
    }

    @Test
    @Transactional
    void getNonExistingChild() throws Exception {
        // Get the child
        restChildMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewChild() throws Exception {
        // Initialize the database
        childRepository.saveAndFlush(child);

        int databaseSizeBeforeUpdate = childRepository.findAll().size();

        // Update the child
        Child updatedChild = childRepository.findById(child.getId()).get();
        // Disconnect from session so that the updates on updatedChild are not directly saved in db
        em.detach(updatedChild);
        updatedChild
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .name(UPDATED_NAME)
            .age(UPDATED_AGE)
            .gender(UPDATED_GENDER)
            .birthdate(UPDATED_BIRTHDATE)
            .status(UPDATED_STATUS)
            .agency(UPDATED_AGENCY);
        ChildDTO childDTO = childMapper.toDto(updatedChild);

        restChildMockMvc
            .perform(
                put(ENTITY_API_URL_ID, childDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(childDTO))
            )
            .andExpect(status().isOk());

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeUpdate);
        Child testChild = childList.get(childList.size() - 1);
        assertThat(testChild.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testChild.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testChild.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testChild.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testChild.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testChild.getBirthdate()).isEqualTo(UPDATED_BIRTHDATE);
        assertThat(testChild.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testChild.getAgency()).isEqualTo(UPDATED_AGENCY);
    }

    @Test
    @Transactional
    void putNonExistingChild() throws Exception {
        int databaseSizeBeforeUpdate = childRepository.findAll().size();
        child.setId(count.incrementAndGet());

        // Create the Child
        ChildDTO childDTO = childMapper.toDto(child);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChildMockMvc
            .perform(
                put(ENTITY_API_URL_ID, childDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(childDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchChild() throws Exception {
        int databaseSizeBeforeUpdate = childRepository.findAll().size();
        child.setId(count.incrementAndGet());

        // Create the Child
        ChildDTO childDTO = childMapper.toDto(child);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChildMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(childDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamChild() throws Exception {
        int databaseSizeBeforeUpdate = childRepository.findAll().size();
        child.setId(count.incrementAndGet());

        // Create the Child
        ChildDTO childDTO = childMapper.toDto(child);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChildMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(childDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateChildWithPatch() throws Exception {
        // Initialize the database
        childRepository.saveAndFlush(child);

        int databaseSizeBeforeUpdate = childRepository.findAll().size();

        // Update the child using partial update
        Child partialUpdatedChild = new Child();
        partialUpdatedChild.setId(child.getId());

        partialUpdatedChild.image(UPDATED_IMAGE).imageContentType(UPDATED_IMAGE_CONTENT_TYPE).age(UPDATED_AGE);

        restChildMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChild.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChild))
            )
            .andExpect(status().isOk());

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeUpdate);
        Child testChild = childList.get(childList.size() - 1);
        assertThat(testChild.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testChild.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testChild.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testChild.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testChild.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testChild.getBirthdate()).isEqualTo(DEFAULT_BIRTHDATE);
        assertThat(testChild.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testChild.getAgency()).isEqualTo(DEFAULT_AGENCY);
    }

    @Test
    @Transactional
    void fullUpdateChildWithPatch() throws Exception {
        // Initialize the database
        childRepository.saveAndFlush(child);

        int databaseSizeBeforeUpdate = childRepository.findAll().size();

        // Update the child using partial update
        Child partialUpdatedChild = new Child();
        partialUpdatedChild.setId(child.getId());

        partialUpdatedChild
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .name(UPDATED_NAME)
            .age(UPDATED_AGE)
            .gender(UPDATED_GENDER)
            .birthdate(UPDATED_BIRTHDATE)
            .status(UPDATED_STATUS)
            .agency(UPDATED_AGENCY);

        restChildMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChild.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChild))
            )
            .andExpect(status().isOk());

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeUpdate);
        Child testChild = childList.get(childList.size() - 1);
        assertThat(testChild.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testChild.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testChild.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testChild.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testChild.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testChild.getBirthdate()).isEqualTo(UPDATED_BIRTHDATE);
        assertThat(testChild.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testChild.getAgency()).isEqualTo(UPDATED_AGENCY);
    }

    @Test
    @Transactional
    void patchNonExistingChild() throws Exception {
        int databaseSizeBeforeUpdate = childRepository.findAll().size();
        child.setId(count.incrementAndGet());

        // Create the Child
        ChildDTO childDTO = childMapper.toDto(child);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChildMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, childDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(childDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchChild() throws Exception {
        int databaseSizeBeforeUpdate = childRepository.findAll().size();
        child.setId(count.incrementAndGet());

        // Create the Child
        ChildDTO childDTO = childMapper.toDto(child);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChildMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(childDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamChild() throws Exception {
        int databaseSizeBeforeUpdate = childRepository.findAll().size();
        child.setId(count.incrementAndGet());

        // Create the Child
        ChildDTO childDTO = childMapper.toDto(child);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChildMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(childDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteChild() throws Exception {
        // Initialize the database
        childRepository.saveAndFlush(child);

        int databaseSizeBeforeDelete = childRepository.findAll().size();

        // Delete the child
        restChildMockMvc
            .perform(delete(ENTITY_API_URL_ID, child.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
