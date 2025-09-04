package com.kydas.build.users;

import com.kydas.build.core.crud.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User> {
    Optional<User> findByLogin(String login);

    Boolean existsByLogin(String login);
}