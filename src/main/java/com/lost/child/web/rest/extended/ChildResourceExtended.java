package com.lost.child.web.rest.extended;

import com.lost.child.repository.ChildRepository;
import com.lost.child.service.ChildService;
import com.lost.child.service.dto.ChildDTO;
import com.lost.child.service.dto.extended.ChildDTOExtended;
import com.lost.child.service.extended.ChildServiceExtended;
import com.lost.child.web.rest.ChildResource;
import com.lost.child.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.lost.child.domain.Child}.
 */
@RestController
@RequestMapping("/api/v1")
public class ChildResourceExtended extends ChildResource {

    private final Logger log = LoggerFactory.getLogger(ChildResource.class);

    private static final String ENTITY_NAME = "child";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChildServiceExtended childServiceExtended;

    private final ChildRepository childRepository;

    public ChildResourceExtended(ChildServiceExtended childServiceExtended, ChildRepository childRepository) {
        super(childServiceExtended, childRepository);
        this.childServiceExtended = childServiceExtended;
        this.childRepository = childRepository;
    }

    /**
     * {@code GET  /children/:id} : get the "id" child.
     *
     * @param id the id of the childDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the childDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/children-extended/{id}")
    public ResponseEntity<ChildDTO> getChild(@PathVariable Long id) {
        log.debug("REST request to get Child : {}", id);
        Optional<ChildDTO> childDTO = childServiceExtended.findOne(id);
        return ResponseUtil.wrapOrNotFound(childDTO);
    }

    /**
     * {@code GET  /children} : get all the children.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of children in body.
     */
    @GetMapping("/children-extended")
    public ResponseEntity<List<ChildDTO>> getAllChildren(Pageable pageable) {
        log.debug("REST request to get a page of Children");
        Page<ChildDTO> page = childServiceExtended.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code POST  /children} : Create a new child.
     *
     * @param childDTO the childDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new childDTO, or with status {@code 400 (Bad Request)} if the child has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/children-extended")
    public ResponseEntity<ChildDTOExtended> createChild(@Valid @RequestBody ChildDTOExtended childDTO) throws URISyntaxException {
        log.debug("REST request to save Child : {}", childDTO);
        if (childDTO.getId() != null) {
            throw new BadRequestAlertException("A new child cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChildDTOExtended result = childServiceExtended.save(childDTO);
        return ResponseEntity
            .created(new URI("/api/children-extended/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /children-extended/:id} : Updates an existing child.
     *
     * @param id the id of the childDTO to save.
     * @param childDTO the childDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated childDTO,
     * or with status {@code 400 (Bad Request)} if the childDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the childDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/children-extended/{id}")
    public ResponseEntity<ChildDTOExtended> updateChild(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ChildDTOExtended childDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Child : {}, {}", id, childDTO);
        if (childDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, childDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!childRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ChildDTOExtended result = childServiceExtended.save(childDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, childDTO.getId().toString()))
            .body(result);
    }
}
