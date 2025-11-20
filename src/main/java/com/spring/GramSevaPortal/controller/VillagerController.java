package com.spring.GramSevaPortal.controller;

import com.spring.GramSevaPortal.DTO.RegisterRequest;
import com.spring.GramSevaPortal.model.Complain;
import com.spring.GramSevaPortal.model.User;
import com.spring.GramSevaPortal.service.ComplainService;
import com.spring.GramSevaPortal.service.VillagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/villager")
public class VillagerController {
        @Autowired
        private VillagerService villagerService;
        @Autowired
        private ComplainService complainService;
        @PostMapping("/add")
        public void saveUser(@RequestBody User request){
            villagerService.save(request);
        }

        @GetMapping("/getUser")
        public User getUser(Authentication authentication){
                String email = authentication.getName();
                return villagerService.getLoggedInUser(email);
        }

        @PutMapping("/update")
        public ResponseEntity<?> updateUser(@RequestBody User user,Authentication authentication){
                if(user == null){
                        return ResponseEntity.ok("Failed to update profile");
                }
                User loggedInUser = villagerService.getLoggedInUser(authentication.getName());
                loggedInUser.setFirstName(user.getFirstName());
                loggedInUser.setLastName(user.getLastName());
                loggedInUser.setEmail(user.getEmail());
                loggedInUser.setMobileNo(user.getMobileNo());
                loggedInUser.setGender(user.getGender());
                loggedInUser.setVillageWardNo(user.getVillageWardNo());

                villagerService.save(loggedInUser);
                return ResponseEntity.ok(loggedInUser);
        }
//        @GetMapping("/my")
//        @PreAuthorize("hasRole = VILLAGER")
//        public ResponseEntity<?> getAllComplainOfUser(Authentication authentication){
//                User user = villagerService.getLoggedInUser(authentication.getName());
//                List<Complain> complainList = complainService.getComplaintByUser(user);
//                return ResponseEntity.ok(complainList);
//        }
        @PostMapping("/saveComplain")
        public ResponseEntity<String> addComplaint(
                @RequestParam("category") String category,
                @RequestParam("description") String description,
                @RequestParam("landMark") String landMark,
                @RequestParam(value = "image", required = false) MultipartFile image,
                Authentication authentication) {

                String email = authentication.getName();
                complainService.addComplain(category, description, landMark, image, email);
                return ResponseEntity.ok("Complaint registered successfully");
        }
        @GetMapping("/my-complains")
        public ResponseEntity<?> getMyComplains(Authentication authentication){
                User user = villagerService.getLoggedInUser(authentication.getName());
                List<Complain> complains = complainService.getComplaintByUser(user);
                return ResponseEntity.ok(complains);
        }

        // Spring Boot Backend - MUST have proper authorization
        @PreAuthorize("hasRole('VILLAGER')")
        @DeleteMapping("/complain/{id}")
        public ResponseEntity<?> deleteComplaint(@PathVariable Long id,
                                                Authentication authentication) {

                // Check if complaint exists and belongs to the user
                User user = villagerService.getLoggedInUser(authentication.getName());
                Complain complain = complainService.getComplaintById(id);
                // Authorization check - user can only delete their own complaints
                if (! (complain.getUser().getId() == user.getId())){
                        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .body("You can only delete your own complaints");
                }

                complainService.deleteComplaint(id);
                return ResponseEntity.ok().build();
        }

}
