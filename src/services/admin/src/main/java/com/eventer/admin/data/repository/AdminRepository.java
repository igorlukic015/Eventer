package com.eventer.admin.data.repository;

import com.eventer.admin.data.model.Admin;
import com.eventer.admin.service.domain.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByUsername(String username);

    boolean existsByUsername(String username);

    Page<Admin> findAllByUsernameContainingIgnoreCaseAndRoleEqualsIgnoreCase(String searchTerm, String role, Pageable pageable);
}
