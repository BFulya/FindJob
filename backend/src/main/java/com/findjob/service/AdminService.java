package com.findjob.service;

import com.findjob.dto.AdminDto;

/**
 * Service interface for admin operations
 * Follows Interface Segregation Principle
 */
public interface AdminService {

    AdminDto getAdminById(Long id);

    AdminDto getAdminByUsername(String username);

    AdminDto updateAdmin(Long id, AdminDto adminDto);
}
