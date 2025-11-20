package com.spring.GramSevaPortal.repository;

import com.spring.GramSevaPortal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VillagerRepository extends JpaRepository<User,Long>{
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
