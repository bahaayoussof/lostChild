package com.lost.child.service;

import com.lost.child.domain.LastSeenAddress;
import com.lost.child.repository.LastSeenAddressRepository;
import com.lost.child.service.dto.LastSeenAddressDTO;
import com.lost.child.service.mapper.LastSeenAddressMapper;
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
 * Service Implementation for managing {@link LastSeenAddress}.
 */
@Service
@Transactional
public class LastSeenAddressService {

    private final Logger log = LoggerFactory.getLogger(LastSeenAddressService.class);

    private final LastSeenAddressRepository lastSeenAddressRepository;

    private final LastSeenAddressMapper lastSeenAddressMapper;

    public LastSeenAddressService(LastSeenAddressRepository lastSeenAddressRepository, LastSeenAddressMapper lastSeenAddressMapper) {
        this.lastSeenAddressRepository = lastSeenAddressRepository;
        this.lastSeenAddressMapper = lastSeenAddressMapper;
    }

    /**
     * Save a lastSeenAddress.
     *
     * @param lastSeenAddressDTO the entity to save.
     * @return the persisted entity.
     */
    public LastSeenAddressDTO save(LastSeenAddressDTO lastSeenAddressDTO) {
        log.debug("Request to save LastSeenAddress : {}", lastSeenAddressDTO);
        LastSeenAddress lastSeenAddress = lastSeenAddressMapper.toEntity(lastSeenAddressDTO);
        lastSeenAddress = lastSeenAddressRepository.save(lastSeenAddress);
        return lastSeenAddressMapper.toDto(lastSeenAddress);
    }

    /**
     * Partially update a lastSeenAddress.
     *
     * @param lastSeenAddressDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LastSeenAddressDTO> partialUpdate(LastSeenAddressDTO lastSeenAddressDTO) {
        log.debug("Request to partially update LastSeenAddress : {}", lastSeenAddressDTO);

        return lastSeenAddressRepository
            .findById(lastSeenAddressDTO.getId())
            .map(existingLastSeenAddress -> {
                lastSeenAddressMapper.partialUpdate(existingLastSeenAddress, lastSeenAddressDTO);

                return existingLastSeenAddress;
            })
            .map(lastSeenAddressRepository::save)
            .map(lastSeenAddressMapper::toDto);
    }

    /**
     * Get all the lastSeenAddresses.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<LastSeenAddressDTO> findAll() {
        log.debug("Request to get all LastSeenAddresses");
        return lastSeenAddressRepository
            .findAll()
            .stream()
            .map(lastSeenAddressMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the lastSeenAddresses where LastSeen is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<LastSeenAddressDTO> findAllWhereLastSeenIsNull() {
        log.debug("Request to get all lastSeenAddresses where LastSeen is null");
        return StreamSupport
            .stream(lastSeenAddressRepository.findAll().spliterator(), false)
            .filter(lastSeenAddress -> lastSeenAddress.getLastSeen() == null)
            .map(lastSeenAddressMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one lastSeenAddress by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LastSeenAddressDTO> findOne(Long id) {
        log.debug("Request to get LastSeenAddress : {}", id);
        return lastSeenAddressRepository.findById(id).map(lastSeenAddressMapper::toDto);
    }

    /**
     * Delete the lastSeenAddress by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LastSeenAddress : {}", id);
        lastSeenAddressRepository.deleteById(id);
    }
}
