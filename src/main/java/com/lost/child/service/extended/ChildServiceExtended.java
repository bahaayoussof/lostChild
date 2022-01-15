package com.lost.child.service.extended;

import com.lost.child.domain.Child;
import com.lost.child.domain.LastSeen;
import com.lost.child.repository.LastSeenRepository;
import com.lost.child.repository.extended.ChildRepositoryExtended;
import com.lost.child.service.ChildService;
import com.lost.child.service.dto.ChildDTO;
import com.lost.child.service.dto.extended.ChildDTOExtended;
import com.lost.child.service.mapper.ChildMapper;
import com.lost.child.service.mapper.extended.ChildMapperExtended;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Child}.
 */
@Service
@Transactional
public class ChildServiceExtended extends ChildService {

    private final Logger log = LoggerFactory.getLogger(ChildServiceExtended.class);

    private final ChildRepositoryExtended childRepositoryExtended;

    private final ChildMapper childMapper;
    private final ChildMapperExtended childMapperExtended;
    private final LastSeenRepository lastSeenRepository;

    public ChildServiceExtended(
        ChildRepositoryExtended childRepositoryExtended,
        ChildMapperExtended childMapperExtended,
        ChildMapper childMapper,
        LastSeenRepository lastSeenRepository
    ) {
        super(childRepositoryExtended, childMapper);
        this.childRepositoryExtended = childRepositoryExtended;
        this.childMapperExtended = childMapperExtended;
        this.childMapper = childMapper;
        this.lastSeenRepository = lastSeenRepository;
    }

    /**
     * Save a child.
     *
     * @param childDTO the entity to save.
     * @return the persisted entity.
     */
    public ChildDTOExtended save(ChildDTOExtended childDTO) {
        log.debug("Request to save Child : {}", childDTO);
        Child child = childMapperExtended.toEntity(childDTO);
        Child savedChild = childRepositoryExtended.save(child);
        lastSeenRepository.saveAll(child.getLastSeens());
        return childMapperExtended.toDto(child);
    }

    /**
     * Partially update a child.
     *
     * @param childDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ChildDTOExtended> partialUpdate(ChildDTOExtended childDTO) {
        log.debug("Request to partially update Child : {}", childDTO);

        return childRepositoryExtended
            .findById(childDTO.getId())
            .map(existingChild -> {
                childMapperExtended.partialUpdate(existingChild, childDTO);

                return existingChild;
            })
            .map(childRepositoryExtended::save)
            .map(childMapperExtended::toDto);
    }

    /**
     * Get all the children.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ChildDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Children");
        return childRepositoryExtended.findAll(pageable).map(childMapperExtended::toDto);
    }

    /**
     * Get one child by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ChildDTO> findOne(Long id) {
        log.debug("Request to get Child : {}", id);
        return childRepositoryExtended.findOneWithEagerRelationships(id).map(childMapperExtended::toDto);
    }

    /**
     * Delete the child by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Child : {}", id);
        childRepositoryExtended.deleteById(id);
    }
}
