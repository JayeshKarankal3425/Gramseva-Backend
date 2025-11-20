package com.spring.GramSevaPortal.controller;

import com.spring.GramSevaPortal.DTO.UpdateStatusRequest;
import com.spring.GramSevaPortal.model.Complain;
import com.spring.GramSevaPortal.model.User;
import com.spring.GramSevaPortal.service.ComplainService;
import com.spring.GramSevaPortal.service.VillagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class adminController {
    @Autowired
    private ComplainService complaintService;

    @Autowired
    private VillagerService villagerService;

    @GetMapping("/getAll")
    public ResponseEntity<List<Complain>> getAllComplaints(){
        return ResponseEntity.ok().body(complaintService.allComplaints());
    }
    @GetMapping("/villagers/all")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(villagerService.getALlUser());
    }
    @PostMapping("/complain/status")
    public ResponseEntity<?> updateStatus(@RequestBody UpdateStatusRequest updateStatusRequest,
                                               Authentication authentication){
        Complain complain = complaintService.updateComplaintStatus(
                                                updateStatusRequest.getComplaintId(),
                                                updateStatusRequest.getUpdatedStatus()
                                                );
        return ResponseEntity.ok(complain);
    }

    @DeleteMapping("/delete/villager/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id,Authentication auth){
        User user = villagerService.getUserById(id);
        villagerService.deleteUser(user);
        return ResponseEntity.ok("Villager deleted successfully");
    }
}
