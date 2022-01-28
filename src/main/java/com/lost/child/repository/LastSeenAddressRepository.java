package com.lost.child.repository;

import com.lost.child.domain.LastSeenAddress;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LastSeenAddress entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LastSeenAddressRepository extends JpaRepository<LastSeenAddress, Long> {}
