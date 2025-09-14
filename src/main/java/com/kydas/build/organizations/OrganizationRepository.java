package com.kydas.build.organizations;

import com.kydas.build.core.crud.BaseRepository;
import com.kydas.build.core.exceptions.classes.NotFoundException;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrganizationRepository extends BaseRepository<Organization> {
    default Organization findByIdOrElseThrow(UUID id) throws NotFoundException {
        return findById(id).orElseThrow(NotFoundException::new);
    }
}
