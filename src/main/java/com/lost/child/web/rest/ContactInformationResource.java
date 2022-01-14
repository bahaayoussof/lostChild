package com.lost.child.web.rest;

import com.lost.child.repository.ContactInformationRepository;
import com.lost.child.service.ContactInformationService;
import com.lost.child.service.dto.ContactInformationDTO;
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
 * REST controller for managing {@link com.lost.child.domain.ContactInformation}.
 */
@RestController
@RequestMapping("/api")
public class ContactInformationResource {

    private final Logger log = LoggerFactory.getLogger(ContactInformationResource.class);

    private static final String ENTITY_NAME = "contactInformation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContactInformationService contactInformationService;

    private final ContactInformationRepository contactInformationRepository;

    public ContactInformationResource(
        ContactInformationService contactInformationService,
        ContactInformationRepository contactInformationRepository
    ) {
        this.contactInformationService = contactInformationService;
        this.contactInformationRepository = contactInformationRepository;
    }

    /**
     * {@code POST  /contact-informations} : Create a new contactInformation.
     *
     * @param contactInformationDTO the contactInformationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contactInformationDTO, or with status {@code 400 (Bad Request)} if the contactInformation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contact-informations")
    public ResponseEntity<ContactInformationDTO> createContactInformation(@Valid @RequestBody ContactInformationDTO contactInformationDTO)
        throws URISyntaxException {
        log.debug("REST request to save ContactInformation : {}", contactInformationDTO);
        if (contactInformationDTO.getId() != null) {
            throw new BadRequestAlertException("A new contactInformation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContactInformationDTO result = contactInformationService.save(contactInformationDTO);
        return ResponseEntity
            .created(new URI("/api/contact-informations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contact-informations/:id} : Updates an existing contactInformation.
     *
     * @param id the id of the contactInformationDTO to save.
     * @param contactInformationDTO the contactInformationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactInformationDTO,
     * or with status {@code 400 (Bad Request)} if the contactInformationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contactInformationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contact-informations/{id}")
    public ResponseEntity<ContactInformationDTO> updateContactInformation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContactInformationDTO contactInformationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ContactInformation : {}, {}", id, contactInformationDTO);
        if (contactInformationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactInformationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contactInformationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ContactInformationDTO result = contactInformationService.save(contactInformationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contactInformationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /contact-informations/:id} : Partial updates given fields of an existing contactInformation, field will ignore if it is null
     *
     * @param id the id of the contactInformationDTO to save.
     * @param contactInformationDTO the contactInformationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactInformationDTO,
     * or with status {@code 400 (Bad Request)} if the contactInformationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the contactInformationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the contactInformationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/contact-informations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContactInformationDTO> partialUpdateContactInformation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContactInformationDTO contactInformationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ContactInformation partially : {}, {}", id, contactInformationDTO);
        if (contactInformationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactInformationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contactInformationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContactInformationDTO> result = contactInformationService.partialUpdate(contactInformationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contactInformationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /contact-informations} : get all the contactInformations.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contactInformations in body.
     */
    @GetMapping("/contact-informations")
    public List<ContactInformationDTO> getAllContactInformations(@RequestParam(required = false) String filter) {
        if ("child-is-null".equals(filter)) {
            log.debug("REST request to get all ContactInformations where child is null");
            return contactInformationService.findAllWhereChildIsNull();
        }
        log.debug("REST request to get all ContactInformations");
        return contactInformationService.findAll();
    }

    /**
     * {@code GET  /contact-informations/:id} : get the "id" contactInformation.
     *
     * @param id the id of the contactInformationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contactInformationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contact-informations/{id}")
    public ResponseEntity<ContactInformationDTO> getContactInformation(@PathVariable Long id) {
        log.debug("REST request to get ContactInformation : {}", id);
        Optional<ContactInformationDTO> contactInformationDTO = contactInformationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contactInformationDTO);
    }

    /**
     * {@code DELETE  /contact-informations/:id} : delete the "id" contactInformation.
     *
     * @param id the id of the contactInformationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contact-informations/{id}")
    public ResponseEntity<Void> deleteContactInformation(@PathVariable Long id) {
        log.debug("REST request to delete ContactInformation : {}", id);
        contactInformationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
