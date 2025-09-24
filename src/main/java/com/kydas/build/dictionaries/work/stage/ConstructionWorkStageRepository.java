package com.kydas.build.dictionaries.work.stage;

import com.kydas.build.core.crud.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ConstructionWorkStageRepository extends BaseRepository<ConstructionWorkStage> {
    List<ConstructionWorkStage> findByWorkId(UUID workId);
}
