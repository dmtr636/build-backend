package com.kydas.build.core.crud;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.UUID;

@NoRepositoryBean
public interface BaseRepository<E> extends JpaRepository<E, UUID>, JpaSpecificationExecutor<E> {
    boolean existsByIdIsNotNull();
}
