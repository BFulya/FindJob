package com.findjob.service.impl;

import com.findjob.dto.AdminDto;
import com.findjob.entity.Admin;
import com.findjob.exception.ResourceNotFoundException;
import com.findjob.repository.AdminRepository;
import com.findjob.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of AdminService
 * Implements business logic for admin operations
 * Follows Single Responsibility Principle
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    @Override
    public AdminDto getAdminById(Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));
        return convertToDto(admin);
    }

    @Override
    public AdminDto getAdminByUsername(String username) {
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));
        return convertToDto(admin);
    }

    @Override
    @Transactional
    public AdminDto updateAdmin(Long id, AdminDto adminDto) {
        log.info("Updating admin with ID: {}", id);

        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));

        admin.setFirstName(adminDto.getFirstName());
        admin.setLastName(adminDto.getLastName());
        admin.setPhoneNumber(adminDto.getPhoneNumber());
        admin.setCompanyName(adminDto.getCompanyName());
        admin.setCompanyAddress(adminDto.getCompanyAddress());
        admin.setTaxNumber(adminDto.getTaxNumber());

        Admin updatedAdmin = adminRepository.save(admin);
        log.info("Admin updated successfully with ID: {}", updatedAdmin.getId());

        return convertToDto(updatedAdmin);
    }

    private AdminDto convertToDto(Admin admin) {
        AdminDto dto = new AdminDto();
        dto.setId(admin.getId());
        dto.setUsername(admin.getUsername());
        dto.setFirstName(admin.getFirstName());
        dto.setLastName(admin.getLastName());
        dto.setEmail(admin.getEmail());
        dto.setPhoneNumber(admin.getPhoneNumber());
        dto.setRole(admin.getRole());
        dto.setCompanyName(admin.getCompanyName());
        dto.setCompanyAddress(admin.getCompanyAddress());
        dto.setTaxNumber(admin.getTaxNumber());
        return dto;
    }
}
