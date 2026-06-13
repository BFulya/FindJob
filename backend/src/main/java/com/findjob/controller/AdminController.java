package com.findjob.controller;

import com.findjob.dto.AdminDto;
import com.findjob.security.JwtTokenProvider;
import com.findjob.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for admin operations
 * Follows RESTful API design principles
 */
@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
@Tag(name = "Admins", description = "Admin management APIs")
public class AdminController {

    private final AdminService adminService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/{id}")
    @Operation(summary = "Get admin by ID", description = "Retrieves an admin by their ID")
    public ResponseEntity<AdminDto> getAdminById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getAdminById(id));
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get current admin profile", description = "Retrieves the current admin's profile")
    public ResponseEntity<AdminDto> getCurrentAdmin(@RequestHeader("Authorization") String token) {
        String username = jwtTokenProvider.getUsernameFromToken(token.substring(7));
        return ResponseEntity.ok(adminService.getAdminByUsername(username));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update admin profile", description = "Updates an admin's profile")
    public ResponseEntity<AdminDto> updateAdmin(@PathVariable Long id,
                                               @Valid @RequestBody AdminDto adminDto) {
        return ResponseEntity.ok(adminService.updateAdmin(id, adminDto));
    }
}
