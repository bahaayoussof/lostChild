package com.lost.child.service;

import com.lost.child.domain.ContactInformation;
import com.lost.child.repository.ContactInformationRepository;
import com.lost.child.service.dto.ContactInformationDTO;
import com.lost.child.service.mapper.ContactInformationMapper;
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
 * Service Implementation for managing {@link ContactInformation}.
 */
@Service
@Transactional
public class ContactInformationService {

    private final Logger log = LoggerFactory.getLogger(ContactInformationService.class);

    private final ContactInformationRepository contactInformationRepository;

    private final ContactInformationMapper contactInformationMapper;

    public ContactInformationService(
        ContactInformationRepository contactInformationRepository,
        ContactInformationMapper contactInformationMapper
    ) {
        this.contactInformationRepository = contactInformationRepository;
        this.contactInformationMapper = contactInformationMapper;
    }

    /**
     * Save a contactInformation.
     *
     * @param contactInformationDTO the entity to save.
     * @return the persisted entity.
     */
    public ContactInformationDTO save(ContactInformationDTO contactInformationDTO) {
        log.debug("Request to save ContactInformation : {}", contactInformationDTO);
        ContactInformation contactInformation = contactInformationMapper.toEntity(contactInformationDTO);
        contactInformation = contactInformationRepository.save(contactInformation);
        return contactInformationMapper.toDto(contactInformation);
    }

    /**
     * Partially update a contactInformation.
     *
     * @param contactInformationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ContactInformationDTO> partialUpdate(ContactInformationDTO contactInformationDTO) {
        log.debug("Request to partially update ContactInformation : {}", contactInformationDTO);

        return contactInformationRepository
            .findById(contactInformationDTO.getId())
            .map(existingContactInformation -> {
                contactInformationMapper.partialUpdate(existingContactInformation, contactInformationDTO);

                return existingContactInformation;
            })
            .map(contactInformationRepository::save)
            .map(contactInformationMapper::toDto);
    }

    /**
     * Get all the contactInformations.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ContactInformationDTO> findAll() {
        log.debug("Request to get all ContactInformations");
        return contactInformationRepository
            .findAll()
            .stream()
            .map(contactInformationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the contactInformations where Child is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ContactInformationDTO> findAllWhereChildIsNull() {
        log.debug("Request to get all contactInformations where Child is null");
        return StreamSupport
            .stream(contactInformationRepository.findAll().spliterator(), false)
            .filter(contactInformation -> contactInformation.getChild() == null)
            .map(contactInformationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one contactInformation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ContactInformationDTO> findOne(Long id) {
        log.debug("Request to get ContactInformation : {}", id);
        return contactInformationRepository.findById(id).map(contactInformationMapper::toDto);
    }

    /**
     * Delete the contactInformation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ContactInformation : {}", id);
        contactInformationRepository.deleteById(id);
    }
}
