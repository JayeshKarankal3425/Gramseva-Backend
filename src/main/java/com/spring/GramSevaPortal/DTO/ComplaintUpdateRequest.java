package com.spring.GramSevaPortal.DTO;


import org.springframework.web.multipart.MultipartFile;

public class ComplaintUpdateRequest {
    private String description;
    private String landMark;
    private MultipartFile image;
    private Boolean removeImage;

    // Constructors
    public ComplaintUpdateRequest() {}

    public ComplaintUpdateRequest(String description, String landMark, MultipartFile image, Boolean removeImage) {
        this.description = description;
        this.landMark = landMark;
        this.image = image;
        this.removeImage = removeImage;
    }

    // Getters and Setters
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLandMark() {
        return landMark;
    }

    public void setLandMark(String landMark) {
        this.landMark = landMark;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public Boolean getRemoveImage() {
        return removeImage;
    }

    public void setRemoveImage(Boolean removeImage) {
        this.removeImage = removeImage;
    }
}