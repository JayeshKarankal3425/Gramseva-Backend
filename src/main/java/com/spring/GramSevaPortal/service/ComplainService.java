package com.spring.GramSevaPortal.service;

import com.spring.GramSevaPortal.DTO.ComplaintUpdateRequest;
import com.spring.GramSevaPortal.DTO.ImageData;
import com.spring.GramSevaPortal.model.Complain;
import com.spring.GramSevaPortal.model.User;
import com.spring.GramSevaPortal.repository.ComplainRepository;
import com.spring.GramSevaPortal.repository.VillagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ComplainService {
    @Autowired
    private ComplainRepository complaintRepository;

    @Autowired
    private ImageUploadService imageService;
    @Autowired
    private VillagerRepository villagerRepository;
    public void addComplain(String category, String description, String landMark,
                            MultipartFile image, String email) {

        User user = villagerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Complain complain = new Complain();
        complain.setCategory(category);
        complain.setDescription(description);
        complain.setLandMark(landMark);
        complain.setStatus("PENDING");
        complain.setUser(user);

        // ✅ Upload image if available
        if (image != null && !image.isEmpty()) {
            ImageData imageData  = imageService.upload(image);
            complain.setImageUrl(imageData.getUrl());
            complain.setImagePublicId(imageData.getId());
        }

        complaintRepository.save(complain);
    }

    public List<Complain> allComplaints() {
        return complaintRepository.findAll();
    }

    public Complain getComplaintById(long id) {
        return complaintRepository.findById(id).orElseThrow(()->new RuntimeException("User with id:"+id+" not found"));
    }

    @Autowired
    private EmailService emailService;

    public Complain updateComplaintStatus(long complaintId, String updatedStatus) {
        Complain complaint = getComplaintById(complaintId);
        if (complaint == null) return null;

        // ✅ Update status
        complaint.setStatus(updatedStatus);
        complaintRepository.save(complaint);

        // ✅ Send Email to User
        User user = complaint.getUser();
        if (user != null && user.getEmail() != null) {
            emailService.sendComplaintStatusEmail(
                    user.getEmail(),
                    user.getFirstName() + " " + user.getLastName(),
                    complaintId,
                    updatedStatus
            );
        }

        return complaint;
    }


    public List<Complain> getComplaintByUser(User user) {
        List<Complain> complains = complaintRepository.findByUser(user);
        return complains;
    }

    public void deleteComplaint(Long id) {
        Complain complain = getComplaintById(id);
        imageService.deleteImage(complain.getImagePublicId());
        complaintRepository.delete(complain);
    }

    public Complain updateComplaint(Long id, ComplaintUpdateRequest updateRequest, String currentImageUrl) {
        Complain complain = getComplaintById(id);
        complain.setDescription(updateRequest.getDescription());
        complain.setLandMark(updateRequest.getLandMark());

        // ✅ Handle image removal
        if (updateRequest.getRemoveImage() != null && updateRequest.getRemoveImage()) {
            if (currentImageUrl != null) {
//                imageService.deleteFile(currentImageUrl);
            }
            complain.setImageUrl(null);
        }

        // ✅ Handle new image upload (independent of removeImage)
        if (updateRequest.getImage() != null && !updateRequest.getImage().isEmpty()) {
            // Delete old image if exists
            if (currentImageUrl != null) {
//                imageService.deleteFile(currentImageUrl);
            }

            ImageData imageData = imageService.upload(updateRequest.getImage());
            complain.setImageUrl(imageData.getUrl());
            complain.setImagePublicId(imageData.getId());
        }

        complaintRepository.save(complain);
        return complain;
    }

//    public void deleteComplaintsOfUser(User user) {
//        complaintRepository.deleteAllByUserId(user.getId());
//    }
}
