package com.spring.GramSevaPortal.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.Arrays;

@Entity
@Table(name = "complaints")
public class Complain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ You can convert to Enum later
    private String category;

    @Column(length = 2000)
    private String description;

    private String status;  // PENDING / RESOLVED / REJECTED / IN_PROGRESS

    private String landMark;

    private LocalDateTime createdAt;

    // ✅ For Image
    private String imageUrl;

    private String imagePublicId;

    // ✅ Relationship with User
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    // ✅ Auto-generate createdAt time
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // ✅ Constructors
    public Complain() {}

    public Complain(String category, String description, String status, String landMark,
                    String imageUrl, User user) {
        this.category = category;
        this.description = description;
        this.status = status;
        this.landMark = landMark;
        this.imageUrl = imageUrl;
        this.user = user;
    }

    // ✅ Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getLandMark() { return landMark; }
    public void setLandMark(String landMark) { this.landMark = landMark; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user;}

    public String getImagePublicId() {
        return imagePublicId;
    }

    public void setImagePublicId(String imagePublicId) {
        this.imagePublicId = imagePublicId;
    }

    @Override
    public String toString() {
        return "Complaint{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", landMark='" + landMark + '\'' +
                ", createdAt=" + createdAt +
                ", imageUrl=" + imageUrl +
                ", user=" + user +
                '}';
    }
}
