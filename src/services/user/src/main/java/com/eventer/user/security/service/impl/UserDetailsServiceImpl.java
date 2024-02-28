package com.eventer.user.security.service.impl;

import com.eventer.user.data.repository.UserRepository;
import com.eventer.user.data.model.User;
import com.eventer.user.security.contracts.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                "USERNAME_NOT_FOUND"));

        return new CustomUserDetails(user.getUsername(), user.getPassword());
    }
}
