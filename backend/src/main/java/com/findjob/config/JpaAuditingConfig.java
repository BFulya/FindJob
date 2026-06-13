package com.findjob.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA Auditing Configuration
 * Enables automatic tracking of creation and modification dates
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
