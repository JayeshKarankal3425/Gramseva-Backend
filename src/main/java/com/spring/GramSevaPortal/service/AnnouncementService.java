package com.spring.GramSevaPortal.service;

import com.spring.GramSevaPortal.DTO.ImageData;
import com.spring.GramSevaPortal.model.Announcement;
import com.spring.GramSevaPortal.repository.AnnouncementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class AnnouncementService {

    @Autowired
    private AnnouncementRepository repo;

    @Autowired
    private ImageUploadService imageService;

    public void create(String title, String message, MultipartFile image, MultipartFile pdf, String adminEmail) {

        Announcement a = new Announcement();
        a.setTitle(title);
        a.setMessage(message);
        a.setCreatedBy(adminEmail);

        // ✅ Upload image if attached
        if (image != null && !image.isEmpty()) {
            ImageData imageData = imageService.upload(image);
            a.setImageUrl(imageData.getUrl());
            a.setImagePublicId(imageData.getId());
        }

        // ✅ Upload PDF if attached
        if (pdf != null && !pdf.isEmpty()) {
            String pdfUrl = imageService.uploadPdf(pdf);  // same upload method works
            a.setPdfUrl(pdfUrl);
        }

        repo.save(a);
    }


    public List<Announcement> getAll() {
        return repo.findAll();
    }

    public Announcement getById(Long id) {
        return repo.findById(id).orElseThrow(()->new RuntimeException("Announcement Not Found"));
    }

    public void deleteAnnouncement(Long id) {
        repo.delete(getById(id));
    }
}
