package com.lost.child.web.rest;

import com.lost.child.repository.LastSeenRepository;
import com.lost.child.service.LastSeenService;
import com.lost.child.service.dto.LastSeenDTO;
import com.lost.child.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link com.lost.child.domain.LastSeen}.
 */
@RestController
@RequestMapping("/api")
public class LastSeenResource {

    private final Logger log = LoggerFactory.getLogger(LastSeenResource.class);

    private static final String ENTITY_NAME = "lastSeen";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LastSeenService lastSeenService;

    private final LastSeenRepository lastSeenRepository;

    public LastSeenResource(LastSeenService lastSeenService, LastSeenRepository lastSeenRepository) {
        this.lastSeenService = lastSeenService;
        this.lastSeenRepository = lastSeenRepository;
    }

    /**
     * {@code POST  /last-seens} : Create a new lastSeen.
     *
     * @param lastSeenDTO the lastSeenDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lastSeenDTO, or with status {@code 400 (Bad Request)} if the lastSeen has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/last-seens")
    public ResponseEntity<LastSeenDTO> createLastSeen(@Valid @RequestBody LastSeenDTO lastSeenDTO) throws URISyntaxException {
        log.debug("REST request to save LastSeen : {}", lastSeenDTO);
        if (lastSeenDTO.getId() != null) {
            throw new BadRequestAlertException("A new lastSeen cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LastSeenDTO result = lastSeenService.save(lastSeenDTO);
        return ResponseEntity
            .created(new URI("/api/last-seens/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /last-seens/:id} : Updates an existing lastSeen.
     *
     * @param id the id of the lastSeenDTO to save.
     * @param lastSeenDTO the lastSeenDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lastSeenDTO,
     * or with status {@code 400 (Bad Request)} if the lastSeenDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lastSeenDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/last-seens/{id}")
    public ResponseEntity<LastSeenDTO> updateLastSeen(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LastSeenDTO lastSeenDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LastSeen : {}, {}", id, lastSeenDTO);
        if (lastSeenDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lastSeenDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lastSeenRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LastSeenDTO result = lastSeenService.save(lastSeenDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lastSeenDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /last-seens/:id} : Partial updates given fields of an existing lastSeen, field will ignore if it is null
     *
     * @param id the id of the lastSeenDTO to save.
     * @param lastSeenDTO the lastSeenDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lastSeenDTO,
     * or with status {@code 400 (Bad Request)} if the lastSeenDTO is not valid,
     * or with status {@code 404 (Not Found)} if the lastSeenDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the lastSeenDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/last-seens/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LastSeenDTO> partialUpdateLastSeen(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LastSeenDTO lastSeenDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LastSeen partially : {}, {}", id, lastSeenDTO);
        if (lastSeenDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lastSeenDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lastSeenRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LastSeenDTO> result = lastSeenService.partialUpdate(lastSeenDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lastSeenDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /last-seens} : get all the lastSeens.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lastSeens in body.
     */
    @GetMapping("/last-seens")
    public List<LastSeenDTO> getAllLastSeens() {
        log.debug("REST request to get all LastSeens");
        return lastSeenService.findAll();
    }

    /**
     * {@code GET  /last-seens/:id} : get the "id" lastSeen.
     *
     * @param id the id of the lastSeenDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lastSeenDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/last-seens/{id}")
    public ResponseEntity<LastSeenDTO> getLastSeen(@PathVariable Long id) {
        log.debug("REST request to get LastSeen : {}", id);
        Optional<LastSeenDTO> lastSeenDTO = lastSeenService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lastSeenDTO);
    }

    /**
     * {@code DELETE  /last-seens/:id} : delete the "id" lastSeen.
     *
     * @param id the id of the lastSeenDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/last-seens/{id}")
    public ResponseEntity<Void> deleteLastSeen(@PathVariable Long id) {
        log.debug("REST request to delete LastSeen : {}", id);
        lastSeenService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
