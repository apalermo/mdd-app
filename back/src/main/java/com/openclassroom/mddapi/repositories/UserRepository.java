package com.openclassroom.mddapi.repositories;

import com.openclassroom.mddapi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByEmailOrName(String email, String name);

    Boolean existsByEmail(String email);

    Boolean existsByName(String name);

    Boolean existsByEmailAndIdNot(String email, Long id);

    Boolean existsByNameAndIdNot(String name, Long id);
}
