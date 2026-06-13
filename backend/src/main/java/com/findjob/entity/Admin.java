package com.findjob.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Admin entity representing company/admin users
 * Inherits from User base class
 */
@Entity
@Table(name = "admins")
@DiscriminatorValue("ADMIN")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Admin extends User {

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String companyAddress;

    @Column(nullable = false)
    private String taxNumber;
}
