package com.example.AddressBookApp.repository;

import com.example.AddressBookApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//Ye ek repository banayenge jo H2 database se interact karega.
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}