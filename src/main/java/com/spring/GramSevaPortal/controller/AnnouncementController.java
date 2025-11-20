package com.spring.GramSevaPortal.controller;

import com.spring.GramSevaPortal.model.Announcement;
import com.spring.GramSevaPortal.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/announcement")
public class AnnouncementController {

    @Autowired
    private AnnouncementService service;

    @PostMapping("/create")
    public ResponseEntity<String> createAnnouncement(
            @RequestParam("title") String title,
            @RequestParam("message") String message,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "pdf", required = false) MultipartFile pdf,
            Authentication auth
    ) {
        String adminEmail = auth.getName();
        service.create(title, message, image, pdf, adminEmail);
        return ResponseEntity.ok("Announcement Created Successfully");
    }


    @GetMapping("/all")
    public List<Announcement> all() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Announcement getAnnouncement(@PathVariable Long id){
        return service.getById(id);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id){
        service.deleteAnnouncement(id);
    }
}
