package com.polymath.payment_gateway.services;

import com.polymath.payment_gateway.exceptions.CustomNotFound;
import com.polymath.payment_gateway.repositories.UserRepository;
import com.polymath.payment_gateway.models.UserPrincipal;
import com.polymath.payment_gateway.models.Users;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException(username));
        return new UserPrincipal(user);
    }


}
