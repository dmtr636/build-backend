package com.kydas.build.organizations;

import com.kydas.build.core.crud.BaseRepository;
import com.kydas.build.core.exceptions.classes.NotFoundException;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrganizationRepository extends BaseRepository<Organization> {

    @EntityGraph(attributePaths = "employees")
    Optional<Organization> findById(UUID id);

    @EntityGraph(attributePaths = "employees")
    List<Organization> findAll();

    default Organization findByIdOrElseThrow(UUID id) throws NotFoundException {
        return findById(id).orElseThrow(NotFoundException::new);
    }
}
