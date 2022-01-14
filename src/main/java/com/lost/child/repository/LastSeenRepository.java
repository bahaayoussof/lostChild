package com.lost.child.repository;

import com.lost.child.domain.LastSeen;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LastSeen entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LastSeenRepository extends JpaRepository<LastSeen, Long> {}
