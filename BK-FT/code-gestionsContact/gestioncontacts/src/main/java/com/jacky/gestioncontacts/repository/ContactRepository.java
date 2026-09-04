
package com.jacky.gestioncontacts.repository;

import com.jacky.gestioncontacts.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {
}


