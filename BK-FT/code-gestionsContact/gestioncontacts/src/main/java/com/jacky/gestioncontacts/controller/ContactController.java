package com.jacky.gestioncontacts.controller;

import com.jacky.gestioncontacts.entity.Contact;
import com.jacky.gestioncontacts.service.ContactService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/contact")
@CrossOrigin("*")
public class ContactController {

    private final ContactService service;

    public ContactController(ContactService service) {
        this.service = service;
    }

    // 🔹 CREATE
   @PostMapping
    public ResponseEntity<Contact> create(@Valid @RequestBody Contact contact) {
        Contact saved = service.save(contact);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
}


    // 🔹 READ ALL
    @GetMapping
    public ResponseEntity<List<Contact>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // 🔹 READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<Contact> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // 🔹 UPDATE
   @PutMapping("/{id}")
    public ResponseEntity<Contact> update(
        @PathVariable Long id,
        @Valid @RequestBody Contact contact) {
    return ResponseEntity.ok(service.update(id, contact));
}


    // 🔹 DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}


