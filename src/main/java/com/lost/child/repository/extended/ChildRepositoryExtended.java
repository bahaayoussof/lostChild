package com.lost.child.repository.extended;

import com.lost.child.domain.Child;
import com.lost.child.repository.ChildRepository;
import java.util.Optional;
import java.util.stream.DoubleStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * Spring Data SQL repository for the Child entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChildRepositoryExtended extends ChildRepository {
    @Query(
        "select child from Child child left join fetch child.description left join fetch child.address left join fetch child.lastSeens where child.id =:id"
    )
    Optional<Child> findOneWithEagerRelationships(@Param("id") Long id);

    @Query(
        value = "select distinct child from Child child left join fetch child.address",
        countQuery = "select count(distinct child) from Child child"
    )
    Page<Child> findAllWithEagerAddressRelation(Pageable pageable);
}
