package com.example.Digital_Wallet_Api.user.services;

import com.example.Digital_Wallet_Api.user.dtos.UserRequestDTO;
import com.example.Digital_Wallet_Api.user.dtos.UserResponseDTO;

import java.util.List;

public interface UserService {
    UserResponseDTO createUser(UserRequestDTO userRequestDTO);
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO getUserById(Long id);
    UserResponseDTO getUserByEmail(String email);
    UserResponseDTO updateUserInfo(Long id,UserRequestDTO userDetailsRequestDTO);
}
