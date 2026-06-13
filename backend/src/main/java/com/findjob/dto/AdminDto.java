package com.findjob.dto;

import com.findjob.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Admin entity
 * Used for displaying admin/company information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDto {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Role role;
    private String companyName;
    private String companyAddress;
    private String taxNumber;
}
