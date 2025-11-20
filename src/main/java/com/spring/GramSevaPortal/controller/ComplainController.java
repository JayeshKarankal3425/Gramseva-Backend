package com.spring.GramSevaPortal.controller;

import com.spring.GramSevaPortal.DTO.ComplaintUpdateRequest;
import com.spring.GramSevaPortal.model.Complain;
import com.spring.GramSevaPortal.model.User;
import com.spring.GramSevaPortal.service.ComplainService;
import com.spring.GramSevaPortal.service.VillagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/complain")
public class ComplainController {
    @Autowired
    private ComplainService complaintService;
    @Autowired
    private VillagerService villagerService;
    @GetMapping("/getComplain/{id}/view")
    public ResponseEntity<Complain> getComplaint(@PathVariable long id){
        return ResponseEntity.ok().body(complaintService.getComplaintById(id));
    }

    @PreAuthorize("hasRole('VILLAGER')")
    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateComplaint(
            @PathVariable Long id,
            @ModelAttribute ComplaintUpdateRequest updateRequest,
            Authentication authentication) {

        try {
            User user = villagerService.getLoggedInUser(authentication.getName());
            Long userId = user.getId();

            Complain existingComplaint = complaintService.getComplaintById(id);

            // Authorization check
            if (!(existingComplaint.getUser().getId() == userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("success", false, "message", "Access denied"));
            }

            // Status validation
            if (!"PENDING".equals(existingComplaint.getStatus())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("success", false, "message", "Only pending complaints can be edited"));
            }

            // Update complaint
            Complain updatedComplaint = complaintService.updateComplaint(
                    id, updateRequest, existingComplaint.getImageUrl());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Complaint updated successfully");
            response.put("complaint", updatedComplaint);

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Error updating complaint"));
        }
    }
}
