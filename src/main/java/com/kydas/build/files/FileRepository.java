package com.kydas.build.files;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FileRepository extends JpaRepository<File, UUID> {
    List<File> findByUserId(UUID userId);

    @Query("SELECT COALESCE(SUM(f.size), 0) FROM File f WHERE f.userId = :userId")
    Long getTotalFileSizeByUserId(@Param("userId") UUID userId);
}
