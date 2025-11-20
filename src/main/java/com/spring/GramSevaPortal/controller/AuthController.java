package com.spring.GramSevaPortal.controller;

import com.spring.GramSevaPortal.model.User;
import com.spring.GramSevaPortal.repository.VillagerRepository;
import com.spring.GramSevaPortal.security.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.spring.GramSevaPortal.DTO.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private AuthenticationManager authManager;
    @Autowired private VillagerRepository userRepo;
    @Autowired private PasswordEncoder encoder;
    @Autowired private JwtService jwtService;

    // ✅ REGISTER
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req, HttpServletResponse response) {

        if (userRepo.existsByEmail(req.getEmail())) {
            return ResponseEntity.badRequest().body("❌ Email already registered");
        }

        // ✅ Restrict valid roles
        if (!req.getRole().equalsIgnoreCase("VILLAGER") &&
                !req.getRole().equalsIgnoreCase("PANCHAYAT")) {
            return ResponseEntity.badRequest().body("❌ Invalid role. Allowed: VILLAGER, PANCHAYAT");
        }

        User u = new User();
        u.setFirstName(req.getFirstName());
        u.setLastName(req.getLastName());
        u.setEmail(req.getEmail());
        u.setMobileNo(req.getMobileNo());
        u.setVillageWardNo(req.getVillageWardNo());
        u.setPassword(encoder.encode(req.getPassword()));
        u.setRole(req.getRole().toUpperCase());
        u.setGender(req.getGender());

        userRepo.save(u);

        // ✅ JWT
        String token = jwtService.generateToken(u.getEmail(), u.getRole());

        // ✅ Cookie name based on role
        String cookieName = u.getRole().equalsIgnoreCase("PANCHAYAT")
                ? "jwt_admin"
                : "jwt_villager";

        ResponseCookie cookie = ResponseCookie.from(cookieName, token)
                .httpOnly(true)
                .path("/")
                .maxAge(24 * 60 * 60)
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok(new JwtResponse(token, u.getEmail(), u.getRole()));
    }

    // ✅ LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req, HttpServletResponse response) {

        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
        );

        User u = userRepo.findByEmail(req.getEmail()).orElseThrow();

        String token = jwtService.generateToken(u.getEmail(), u.getRole());
        String cookieName = u.getRole().equalsIgnoreCase("PANCHAYAT")
                ? "jwt_admin"
                : "jwt_villager";

        ResponseCookie cookie = ResponseCookie.from(cookieName, token)
                .httpOnly(true)
                .path("/")
                .maxAge(24 * 60 * 60)
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());


        return ResponseEntity.ok(new JwtResponse(token, u.getEmail(), u.getRole()));
    }

    // ✅ LOGOUT
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {

        ResponseCookie deleteUser = ResponseCookie.from("jwt_user", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .build();

        ResponseCookie deleteAdmin = ResponseCookie.from("jwt_admin", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, deleteUser.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, deleteAdmin.toString());

        return ResponseEntity.ok("Logged out");
    }

}
