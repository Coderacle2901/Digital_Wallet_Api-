package com.example.Digital_Wallet_Api.user.repositories;

import com.example.Digital_Wallet_Api.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    //find user by email
Optional<User>findByEmail(String email);

// check if user exists by email
boolean existsByEmail(String email);
}
