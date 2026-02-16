package com.example.Digital_Wallet_Api.user.services;

import com.example.Digital_Wallet_Api.exception.ResourceNotFoundException;
import com.example.Digital_Wallet_Api.user.dtos.UserRequestDTO;
import com.example.Digital_Wallet_Api.user.dtos.UserResponseDTO;
import com.example.Digital_Wallet_Api.user.entities.User;
import com.example.Digital_Wallet_Api.user.entities.enums.Role;
import com.example.Digital_Wallet_Api.user.entities.enums.Status;
import com.example.Digital_Wallet_Api.user.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapDto;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper mapDto) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapDto = mapDto;
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
    if(userRepository.existsByEmail(userRequestDTO.getEmail())){
        throw new RuntimeException("User already exists");
    }
    User newUser = User.builder()
            .firstName(userRequestDTO.getFirstName())
            .lastName(userRequestDTO.getLastName())
            .email(userRequestDTO.getEmail())
            .role(Role.USER)
            .status(Status.ACTIVE)
            .passwordHash(passwordEncoder.encode(userRequestDTO.getPassword()))
            .build();
            userRepository.save(newUser);
        return mapDto.map(newUser,UserResponseDTO.class);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(u ->(mapDto.map(u,UserResponseDTO.class)))
                .toList();
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        User matchingUser = userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User with user_id: " + id + " does not exist"));
        return mapDto.map(matchingUser,UserResponseDTO.class);
    }

    @Override
    public UserResponseDTO getUserByEmail(String email) {
        User matchingUser = userRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("User with email: " + email + " does not exist"));
        return mapDto.map(matchingUser,UserResponseDTO.class);
    }

    @Override
    public UserResponseDTO updateUserInfo(Long id, UserRequestDTO userDetailsRequestDTO) {
        User existingUser = userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User with user_id: " + id + " does not exist"));

        // configurations for skipping passwords during mapping
        mapDto.typeMap(UserRequestDTO.class,User.class)
                .addMappings(m ->m.skip(User::setPasswordHash));

        //mapping
        mapDto.map(userDetailsRequestDTO,existingUser);

        if(userDetailsRequestDTO.getPassword() != null && !userDetailsRequestDTO.getPassword().isBlank()){
            existingUser.setPasswordHash(passwordEncoder.encode(userDetailsRequestDTO.getPassword()));
        }

        userRepository.save(existingUser);
        return mapDto.map(existingUser,UserResponseDTO.class);

    }

    @Override
    public void deleteById(Long id) {
        User matchingUser = userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User with user_id: " + id + " does not exist "));
        userRepository.delete(matchingUser);
    }


}
