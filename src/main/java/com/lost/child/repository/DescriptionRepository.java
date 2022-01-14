package com.lost.child.repository;

import com.lost.child.domain.Description;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Description entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DescriptionRepository extends JpaRepository<Description, Long> {}
