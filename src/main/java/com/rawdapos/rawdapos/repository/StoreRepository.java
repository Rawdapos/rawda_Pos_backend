package com.rawdapos.rawdapos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rawdapos.rawdapos.models.Store;

public interface StoreRepository extends JpaRepository<Store, Integer> {
    public Store findByName(String name);
    public Store findByDescription(String description);
    public Store findByAddress(String address);
    public Store findByPhone(String phone);
    public Store findByEmail(String email);
    
}
