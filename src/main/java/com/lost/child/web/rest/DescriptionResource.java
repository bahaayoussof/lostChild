package com.lost.child.web.rest;

import com.lost.child.repository.DescriptionRepository;
import com.lost.child.service.DescriptionService;
import com.lost.child.service.dto.DescriptionDTO;
import com.lost.child.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.lost.child.domain.Description}.
 */
@RestController
@RequestMapping("/api")
public class DescriptionResource {

    private final Logger log = LoggerFactory.getLogger(DescriptionResource.class);

    private static final String ENTITY_NAME = "description";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DescriptionService descriptionService;

    private final DescriptionRepository descriptionRepository;

    public DescriptionResource(DescriptionService descriptionService, DescriptionRepository descriptionRepository) {
        this.descriptionService = descriptionService;
        this.descriptionRepository = descriptionRepository;
    }

    /**
     * {@code POST  /descriptions} : Create a new description.
     *
     * @param descriptionDTO the descriptionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new descriptionDTO, or with status {@code 400 (Bad Request)} if the description has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/descriptions")
    public ResponseEntity<DescriptionDTO> createDescription(@Valid @RequestBody DescriptionDTO descriptionDTO) throws URISyntaxException {
        log.debug("REST request to save Description : {}", descriptionDTO);
        if (descriptionDTO.getId() != null) {
            throw new BadRequestAlertException("A new description cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DescriptionDTO result = descriptionService.save(descriptionDTO);
        return ResponseEntity
            .created(new URI("/api/descriptions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /descriptions/:id} : Updates an existing description.
     *
     * @param id the id of the descriptionDTO to save.
     * @param descriptionDTO the descriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated descriptionDTO,
     * or with status {@code 400 (Bad Request)} if the descriptionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the descriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/descriptions/{id}")
    public ResponseEntity<DescriptionDTO> updateDescription(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DescriptionDTO descriptionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Description : {}, {}", id, descriptionDTO);
        if (descriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, descriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!descriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DescriptionDTO result = descriptionService.save(descriptionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, descriptionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /descriptions/:id} : Partial updates given fields of an existing description, field will ignore if it is null
     *
     * @param id the id of the descriptionDTO to save.
     * @param descriptionDTO the descriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated descriptionDTO,
     * or with status {@code 400 (Bad Request)} if the descriptionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the descriptionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the descriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/descriptions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DescriptionDTO> partialUpdateDescription(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DescriptionDTO descriptionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Description partially : {}, {}", id, descriptionDTO);
        if (descriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, descriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!descriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DescriptionDTO> result = descriptionService.partialUpdate(descriptionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, descriptionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /descriptions} : get all the descriptions.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of descriptions in body.
     */
    @GetMapping("/descriptions")
    public List<DescriptionDTO> getAllDescriptions(@RequestParam(required = false) String filter) {
        if ("child-is-null".equals(filter)) {
            log.debug("REST request to get all Descriptions where child is null");
            return descriptionService.findAllWhereChildIsNull();
        }
        log.debug("REST request to get all Descriptions");
        return descriptionService.findAll();
    }

    /**
     * {@code GET  /descriptions/:id} : get the "id" description.
     *
     * @param id the id of the descriptionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the descriptionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/descriptions/{id}")
    public ResponseEntity<DescriptionDTO> getDescription(@PathVariable Long id) {
        log.debug("REST request to get Description : {}", id);
        Optional<DescriptionDTO> descriptionDTO = descriptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(descriptionDTO);
    }

    /**
     * {@code DELETE  /descriptions/:id} : delete the "id" description.
     *
     * @param id the id of the descriptionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/descriptions/{id}")
    public ResponseEntity<Void> deleteDescription(@PathVariable Long id) {
        log.debug("REST request to delete Description : {}", id);
        descriptionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
