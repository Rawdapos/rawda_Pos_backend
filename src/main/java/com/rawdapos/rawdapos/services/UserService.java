package com.rawdapos.rawdapos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rawdapos.rawdapos.repository.UserRepository;
import com.rawdapos.rawdapos.models.Users;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByUsername(username);
        if (user != null){
            var returnUser = User.withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
            return returnUser;
        }
        return null;
    }
    
}
