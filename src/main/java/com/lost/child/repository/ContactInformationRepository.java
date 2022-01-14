package com.lost.child.repository;

import com.lost.child.domain.ContactInformation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ContactInformation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactInformationRepository extends JpaRepository<ContactInformation, Long> {}
