package com.kydas.build.events;

import com.kydas.build.core.crud.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends BaseRepository<Event> {

}