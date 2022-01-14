package com.lost.child.service;

import com.lost.child.domain.Description;
import com.lost.child.repository.DescriptionRepository;
import com.lost.child.service.dto.DescriptionDTO;
import com.lost.child.service.mapper.DescriptionMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Description}.
 */
@Service
@Transactional
public class DescriptionService {

    private final Logger log = LoggerFactory.getLogger(DescriptionService.class);

    private final DescriptionRepository descriptionRepository;

    private final DescriptionMapper descriptionMapper;

    public DescriptionService(DescriptionRepository descriptionRepository, DescriptionMapper descriptionMapper) {
        this.descriptionRepository = descriptionRepository;
        this.descriptionMapper = descriptionMapper;
    }

    /**
     * Save a description.
     *
     * @param descriptionDTO the entity to save.
     * @return the persisted entity.
     */
    public DescriptionDTO save(DescriptionDTO descriptionDTO) {
        log.debug("Request to save Description : {}", descriptionDTO);
        Description description = descriptionMapper.toEntity(descriptionDTO);
        description = descriptionRepository.save(description);
        return descriptionMapper.toDto(description);
    }

    /**
     * Partially update a description.
     *
     * @param descriptionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DescriptionDTO> partialUpdate(DescriptionDTO descriptionDTO) {
        log.debug("Request to partially update Description : {}", descriptionDTO);

        return descriptionRepository
            .findById(descriptionDTO.getId())
            .map(existingDescription -> {
                descriptionMapper.partialUpdate(existingDescription, descriptionDTO);

                return existingDescription;
            })
            .map(descriptionRepository::save)
            .map(descriptionMapper::toDto);
    }

    /**
     * Get all the descriptions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<DescriptionDTO> findAll() {
        log.debug("Request to get all Descriptions");
        return descriptionRepository.findAll().stream().map(descriptionMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the descriptions where Child is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<DescriptionDTO> findAllWhereChildIsNull() {
        log.debug("Request to get all descriptions where Child is null");
        return StreamSupport
            .stream(descriptionRepository.findAll().spliterator(), false)
            .filter(description -> description.getChild() == null)
            .map(descriptionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one description by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DescriptionDTO> findOne(Long id) {
        log.debug("Request to get Description : {}", id);
        return descriptionRepository.findById(id).map(descriptionMapper::toDto);
    }

    /**
     * Delete the description by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Description : {}", id);
        descriptionRepository.deleteById(id);
    }
}
