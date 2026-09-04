package com.jacky.gestioncontacts.service;

import com.jacky.gestioncontacts.entity.Contact;
import com.jacky.gestioncontacts.repository.ContactRepository;
import com.jacky.gestioncontacts.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {

    private final ContactRepository repository;

    public ContactService(ContactRepository repository) {
        this.repository = repository;
    }

    public Contact save(Contact contact) {
        return repository.save(contact);
    }

    public List<Contact> getAll() {
        return repository.findAll();
    }

    public Contact getById(Long id) {
        return repository.findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Contact non trouvé avec id " + id));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Contact update(Long id, Contact newContact) {
    return repository.findById(id)
            .map(contact -> {
                contact.setNom(newContact.getNom());
                contact.setPrenom(newContact.getPrenom());
                contact.setTelephone(newContact.getTelephone());
                contact.setEmail(newContact.getEmail());
                return repository.save(contact);
            })
            .orElseThrow(() -> new RuntimeException("Contact non trouvé avec id " + id));
}

}

