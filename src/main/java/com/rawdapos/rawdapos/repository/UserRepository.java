package com.rawdapos.rawdapos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rawdapos.rawdapos.models.Users;

public interface UserRepository extends JpaRepository<Users, Integer> {

    public Users findByUsername(String username);
    public Users findByEmail(String email);
    
}
