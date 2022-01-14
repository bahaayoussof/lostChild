package com.lost.child.repository;

import com.lost.child.domain.Child;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Child entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChildRepository extends JpaRepository<Child, Long> {
    @Query("select child from Child child where child.user.login = ?#{principal.username}")
    List<Child> findByUserIsCurrentUser();
}
