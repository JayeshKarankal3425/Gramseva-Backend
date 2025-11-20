package com.spring.GramSevaPortal.model;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "announcements")
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 2000)
    private String message;

    private String imageUrl;  // ✅ optional image (Cloudinary)
    private String pdfUrl;

    private String imagePublicId;
    private LocalDateTime createdAt;

    private String createdBy;  // ✅ Admin email

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Announcement() {}

    public Announcement(String title, String message, String imageUrl,String pdfUrl, String createdBy) {
        this.title = title;
        this.message = message;
        this.imageUrl = imageUrl;
        this.pdfUrl = pdfUrl;
        this.createdBy = createdBy;
    }

    // ✅ Getters & Setters
    // ... (add normally)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getImagePublicId() {
        return imagePublicId;
    }

    public void setImagePublicId(String imagePublicId) {
        this.imagePublicId = imagePublicId;
    }
}
