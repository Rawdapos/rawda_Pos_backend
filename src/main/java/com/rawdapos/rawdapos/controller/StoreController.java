package com.rawdapos.rawdapos.controller;

import java.util.HashMap;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rawdapos.rawdapos.models.Store;
import com.rawdapos.rawdapos.repository.StoreRepository;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/store")
public class StoreController {
    
    private StoreRepository storeRepository;

    public StoreController(StoreRepository storeRepository){
        this.storeRepository = storeRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addStore(@Valid @RequestBody Store store, BindingResult result){
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            var anotherStore = storeRepository.findByName(store.getName());
            var response = new HashMap<String,Object>();
            if (anotherStore != null) {
                response.put("message", "Une boutique avec ce nom existe déjà");
                return ResponseEntity.badRequest().body(response);
            }
            var newStore = storeRepository.save(store);
            response.put("store", newStore);
            response.put("message", "Boutique ajoutée avec succès");

            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error occurred:" + e);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Object> list(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1") int size) {
        
        var response = new HashMap<String,Object>();
        Pageable pageable = PageRequest.of(page, size);
        Page<Store> stores = storeRepository.findAll(pageable);
        response.put("stores", stores.getContent());
        response.put("totalPages", stores.getTotalPages());
        response.put("totalElements", stores.getTotalElements());
        response.put("currentPage", stores.getNumber());
        response.put("size", stores.getSize());
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getById(@PathVariable Integer id) {
        var response = new HashMap<String, Object>();
        var store = storeRepository.findById(id);
        response.put("store", store);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/update/{id}")
    public ResponseEntity<Object> update(@Valid @RequestBody Store store, BindingResult result, @PathVariable Integer id) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        var response = new HashMap<String, Object>();
        var storeToUpdateOptional = storeRepository.findById(id);

        if (storeToUpdateOptional.isEmpty()) {
            response.put("message", "Boutique non trouvée");
            return ResponseEntity.badRequest().body(response);
        }
        try {
            var anotherStore = storeRepository.findByName(store.getName());
            if (anotherStore != null &&  !anotherStore.getId().equals(id) ) {
                response.put("message", "Une boutique avec ce nom existe déjà");
                return ResponseEntity.badRequest().body(response);
            }
            
            var storeToUpdate = storeToUpdateOptional.get();
            storeToUpdate.setName(store.getName());
            storeToUpdate.setAddress(store.getAddress());
            storeToUpdate.setEmail(store.getEmail());
            storeToUpdate.setPhone(store.getPhone());
            storeToUpdate.setDescription(store.getDescription());
            
            var updatedStore = storeRepository.save(storeToUpdate);
            response.put("store", updatedStore);
            response.put("message", "Boutique mise à jour avec succès");
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            response.put("message", "Une erreur est survenue: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    
}
