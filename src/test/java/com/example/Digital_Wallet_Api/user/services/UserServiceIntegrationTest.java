package com.example.Digital_Wallet_Api.user.services;


import com.example.Digital_Wallet_Api.exception.ResourceNotFoundException;
import com.example.Digital_Wallet_Api.user.dtos.UserRequestDTO;
import com.example.Digital_Wallet_Api.user.dtos.UserResponseDTO;
import com.example.Digital_Wallet_Api.user.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Test
    void createUser_shouldSaveUserToDatabase(){

        //------Arrange-----//
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setFirstName("Miracle");
        userRequestDTO.setLastName("Rapheal");
        userRequestDTO.setEmail("Mchikereuba@gmail.com");
        userRequestDTO.setPassword("miracle177!");

        //---------Act------------//

        UserResponseDTO res = userService.createUser(userRequestDTO);

        //----------Assert-----------//

        Assertions.assertNotNull(res);
        assertEquals("Miracle",res.getFirstName());
        assertNotNull(res.getId());
        assertTrue(userRepository.existsByEmail("Mchikereuba@gmail.com"));
    }

    @Test
    void deleteUser_shouldThrowAnExceptionIfUserDoesNot_Exist(){
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setFirstName("Miracle");
        userRequestDTO.setLastName("Rapheal");
        userRequestDTO.setEmail("Mchikereuba@gmail.com");
        userRequestDTO.setPassword("miracle177!");

        UserResponseDTO res = userService.createUser(userRequestDTO);

        userService.deleteById(res.getId() );

        assertThrows(ResourceNotFoundException.class,() -> userService.getUserById(res.getId()));
    }

    @Test
    void getUserByEmail_shouldReturnCorrectUser(){
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setFirstName("Miracle");
        userRequestDTO.setLastName("Rapheal");
        userRequestDTO.setEmail("Mchikereuba@gmail.com");
        userRequestDTO.setPassword("miracle177!");

        userService.createUser(userRequestDTO);
        UserResponseDTO res = userService.getUserByEmail(userRequestDTO.getEmail());

        assertEquals("Miracle", res.getFirstName());
        assertEquals("Rapheal",res.getLastName());
    }


}