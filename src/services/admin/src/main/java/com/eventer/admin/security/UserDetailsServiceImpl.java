package com.eventer.admin.security;

import com.eventer.admin.data.model.Admin;
import com.eventer.admin.data.repository.AdminRepository;
import com.eventer.admin.security.contracts.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AdminRepository adminRepository;

    public UserDetailsServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin =
                this.adminRepository
                        .findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("USERNAME_NOT_FOUND"));

        return new CustomUserDetails(admin.getUsername(), admin.getPassword(), admin.getRole());
    }
}
