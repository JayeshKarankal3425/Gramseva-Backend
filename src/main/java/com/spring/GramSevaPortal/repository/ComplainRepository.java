package com.spring.GramSevaPortal.repository;

import com.spring.GramSevaPortal.model.Complain;
import com.spring.GramSevaPortal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplainRepository extends JpaRepository<Complain,Long> {
    List<Complain> findByUser(User user);

    void deleteAllByUserId(Long id);
}
