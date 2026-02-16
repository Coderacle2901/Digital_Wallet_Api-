package com.example.Digital_Wallet_Api.user.controllers;


import com.example.Digital_Wallet_Api.user.dtos.UserRequestDTO;
import com.example.Digital_Wallet_Api.user.dtos.UserResponseDTO;
import com.example.Digital_Wallet_Api.user.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final  UserService userService;

    @PostMapping("/users")
    public ResponseEntity<UserResponseDTO> addUser(@Valid @RequestBody UserRequestDTO userRequestDTO){
    UserResponseDTO addedUser = userService.createUser(userRequestDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(addedUser);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id){
        UserResponseDTO matchingUser = userService.getUserById(id);
        return ResponseEntity.ok(matchingUser);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(){
        List<UserResponseDTO> userList = userService.getAllUsers();
        return ResponseEntity.ok(userList);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO> updateUserDetails(@Valid @RequestBody UserRequestDTO userRequestDTO,@PathVariable Long id){
        UserResponseDTO updatedUser = userService.updateUserInfo(id,userRequestDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id){
        userService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
