package com.lost.child.service;

import com.lost.child.domain.LastSeen;
import com.lost.child.repository.LastSeenRepository;
import com.lost.child.service.dto.LastSeenDTO;
import com.lost.child.service.mapper.LastSeenMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LastSeen}.
 */
@Service
@Transactional
public class LastSeenService {

    private final Logger log = LoggerFactory.getLogger(LastSeenService.class);

    private final LastSeenRepository lastSeenRepository;

    private final LastSeenMapper lastSeenMapper;

    public LastSeenService(LastSeenRepository lastSeenRepository, LastSeenMapper lastSeenMapper) {
        this.lastSeenRepository = lastSeenRepository;
        this.lastSeenMapper = lastSeenMapper;
    }

    /**
     * Save a lastSeen.
     *
     * @param lastSeenDTO the entity to save.
     * @return the persisted entity.
     */
    public LastSeenDTO save(LastSeenDTO lastSeenDTO) {
        log.debug("Request to save LastSeen : {}", lastSeenDTO);
        LastSeen lastSeen = lastSeenMapper.toEntity(lastSeenDTO);
        lastSeen = lastSeenRepository.save(lastSeen);
        return lastSeenMapper.toDto(lastSeen);
    }

    /**
     * Partially update a lastSeen.
     *
     * @param lastSeenDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LastSeenDTO> partialUpdate(LastSeenDTO lastSeenDTO) {
        log.debug("Request to partially update LastSeen : {}", lastSeenDTO);

        return lastSeenRepository
            .findById(lastSeenDTO.getId())
            .map(existingLastSeen -> {
                lastSeenMapper.partialUpdate(existingLastSeen, lastSeenDTO);

                return existingLastSeen;
            })
            .map(lastSeenRepository::save)
            .map(lastSeenMapper::toDto);
    }

    /**
     * Get all the lastSeens.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<LastSeenDTO> findAll() {
        log.debug("Request to get all LastSeens");
        return lastSeenRepository.findAll().stream().map(lastSeenMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one lastSeen by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LastSeenDTO> findOne(Long id) {
        log.debug("Request to get LastSeen : {}", id);
        return lastSeenRepository.findById(id).map(lastSeenMapper::toDto);
    }

    /**
     * Delete the lastSeen by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LastSeen : {}", id);
        lastSeenRepository.deleteById(id);
    }
}
