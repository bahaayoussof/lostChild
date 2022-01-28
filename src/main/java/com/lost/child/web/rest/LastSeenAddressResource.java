package com.lost.child.web.rest;

import com.lost.child.repository.LastSeenAddressRepository;
import com.lost.child.service.LastSeenAddressService;
import com.lost.child.service.dto.LastSeenAddressDTO;
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
 * REST controller for managing {@link com.lost.child.domain.LastSeenAddress}.
 */
@RestController
@RequestMapping("/api")
public class LastSeenAddressResource {

    private final Logger log = LoggerFactory.getLogger(LastSeenAddressResource.class);

    private static final String ENTITY_NAME = "lastSeenAddress";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LastSeenAddressService lastSeenAddressService;

    private final LastSeenAddressRepository lastSeenAddressRepository;

    public LastSeenAddressResource(LastSeenAddressService lastSeenAddressService, LastSeenAddressRepository lastSeenAddressRepository) {
        this.lastSeenAddressService = lastSeenAddressService;
        this.lastSeenAddressRepository = lastSeenAddressRepository;
    }

    /**
     * {@code POST  /last-seen-addresses} : Create a new lastSeenAddress.
     *
     * @param lastSeenAddressDTO the lastSeenAddressDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lastSeenAddressDTO, or with status {@code 400 (Bad Request)} if the lastSeenAddress has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/last-seen-addresses")
    public ResponseEntity<LastSeenAddressDTO> createLastSeenAddress(@Valid @RequestBody LastSeenAddressDTO lastSeenAddressDTO)
        throws URISyntaxException {
        log.debug("REST request to save LastSeenAddress : {}", lastSeenAddressDTO);
        if (lastSeenAddressDTO.getId() != null) {
            throw new BadRequestAlertException("A new lastSeenAddress cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LastSeenAddressDTO result = lastSeenAddressService.save(lastSeenAddressDTO);
        return ResponseEntity
            .created(new URI("/api/last-seen-addresses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /last-seen-addresses/:id} : Updates an existing lastSeenAddress.
     *
     * @param id the id of the lastSeenAddressDTO to save.
     * @param lastSeenAddressDTO the lastSeenAddressDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lastSeenAddressDTO,
     * or with status {@code 400 (Bad Request)} if the lastSeenAddressDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lastSeenAddressDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/last-seen-addresses/{id}")
    public ResponseEntity<LastSeenAddressDTO> updateLastSeenAddress(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LastSeenAddressDTO lastSeenAddressDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LastSeenAddress : {}, {}", id, lastSeenAddressDTO);
        if (lastSeenAddressDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lastSeenAddressDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lastSeenAddressRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LastSeenAddressDTO result = lastSeenAddressService.save(lastSeenAddressDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lastSeenAddressDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /last-seen-addresses/:id} : Partial updates given fields of an existing lastSeenAddress, field will ignore if it is null
     *
     * @param id the id of the lastSeenAddressDTO to save.
     * @param lastSeenAddressDTO the lastSeenAddressDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lastSeenAddressDTO,
     * or with status {@code 400 (Bad Request)} if the lastSeenAddressDTO is not valid,
     * or with status {@code 404 (Not Found)} if the lastSeenAddressDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the lastSeenAddressDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/last-seen-addresses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LastSeenAddressDTO> partialUpdateLastSeenAddress(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LastSeenAddressDTO lastSeenAddressDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LastSeenAddress partially : {}, {}", id, lastSeenAddressDTO);
        if (lastSeenAddressDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lastSeenAddressDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lastSeenAddressRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LastSeenAddressDTO> result = lastSeenAddressService.partialUpdate(lastSeenAddressDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lastSeenAddressDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /last-seen-addresses} : get all the lastSeenAddresses.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lastSeenAddresses in body.
     */
    @GetMapping("/last-seen-addresses")
    public List<LastSeenAddressDTO> getAllLastSeenAddresses(@RequestParam(required = false) String filter) {
        if ("lastseen-is-null".equals(filter)) {
            log.debug("REST request to get all LastSeenAddresss where lastSeen is null");
            return lastSeenAddressService.findAllWhereLastSeenIsNull();
        }
        log.debug("REST request to get all LastSeenAddresses");
        return lastSeenAddressService.findAll();
    }

    /**
     * {@code GET  /last-seen-addresses/:id} : get the "id" lastSeenAddress.
     *
     * @param id the id of the lastSeenAddressDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lastSeenAddressDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/last-seen-addresses/{id}")
    public ResponseEntity<LastSeenAddressDTO> getLastSeenAddress(@PathVariable Long id) {
        log.debug("REST request to get LastSeenAddress : {}", id);
        Optional<LastSeenAddressDTO> lastSeenAddressDTO = lastSeenAddressService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lastSeenAddressDTO);
    }

    /**
     * {@code DELETE  /last-seen-addresses/:id} : delete the "id" lastSeenAddress.
     *
     * @param id the id of the lastSeenAddressDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/last-seen-addresses/{id}")
    public ResponseEntity<Void> deleteLastSeenAddress(@PathVariable Long id) {
        log.debug("REST request to delete LastSeenAddress : {}", id);
        lastSeenAddressService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
