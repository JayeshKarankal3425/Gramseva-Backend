package com.spring.GramSevaPortal.repository;


import com.spring.GramSevaPortal.model.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

}

