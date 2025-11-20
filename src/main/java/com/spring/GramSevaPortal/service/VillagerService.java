package com.spring.GramSevaPortal.service;

import com.spring.GramSevaPortal.DTO.RegisterRequest;
import com.spring.GramSevaPortal.model.User;
import com.spring.GramSevaPortal.repository.VillagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VillagerService {
    @Autowired
    private VillagerRepository villagerRepository;

    public void save(User user) {
        villagerRepository.save(user);
    }
    public User getLoggedInUser(String email){
        User user = villagerRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("User not Found"));
        return user;
    }

    public List<User> getALlUser() {
        return villagerRepository.findAll();
    }

    public void deleteUser(User user) {
        villagerRepository.delete(user);
    }

    public User getUserById(Long id) {
        return villagerRepository.findById(id).orElseThrow(()->new RuntimeException());
    }
}
