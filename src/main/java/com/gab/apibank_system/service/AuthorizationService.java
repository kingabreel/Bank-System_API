package com.gab.apibank_system.service;

import com.gab.apibank_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Class that holds the users and accounts services (logic).
 *
 * @author kingabreel
 * @since 07-2025
 */
@Service
public class AuthorizationService implements UserDetailsService {
    private final UserRepository repository;

    @Autowired
    public AuthorizationService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        return repository.findByEmail(mail);
    }

}
