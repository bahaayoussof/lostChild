package com.lost.child.service;

import com.lost.child.domain.Child;
import com.lost.child.repository.ChildRepository;
import com.lost.child.service.dto.ChildDTO;
import com.lost.child.service.mapper.ChildMapper;
import java.util.Optional;
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
public class ChildService {

    private final Logger log = LoggerFactory.getLogger(ChildService.class);

    private final ChildRepository childRepository;

    private final ChildMapper childMapper;

    public ChildService(ChildRepository childRepository, ChildMapper childMapper) {
        this.childRepository = childRepository;
        this.childMapper = childMapper;
    }

    /**
     * Save a child.
     *
     * @param childDTO the entity to save.
     * @return the persisted entity.
     */
    public ChildDTO save(ChildDTO childDTO) {
        log.debug("Request to save Child : {}", childDTO);
        Child child = childMapper.toEntity(childDTO);
        child = childRepository.save(child);
        return childMapper.toDto(child);
    }

    /**
     * Partially update a child.
     *
     * @param childDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ChildDTO> partialUpdate(ChildDTO childDTO) {
        log.debug("Request to partially update Child : {}", childDTO);

        return childRepository
            .findById(childDTO.getId())
            .map(existingChild -> {
                childMapper.partialUpdate(existingChild, childDTO);

                return existingChild;
            })
            .map(childRepository::save)
            .map(childMapper::toDto);
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
        return childRepository.findAll(pageable).map(childMapper::toDto);
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
        return childRepository.findById(id).map(childMapper::toDto);
    }

    /**
     * Delete the child by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Child : {}", id);
        childRepository.deleteById(id);
    }
}
