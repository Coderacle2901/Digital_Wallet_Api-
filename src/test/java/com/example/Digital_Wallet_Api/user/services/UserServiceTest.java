package com.example.Digital_Wallet_Api.user.services;

import com.example.Digital_Wallet_Api.exception.ResourceNotFoundException;
import com.example.Digital_Wallet_Api.user.dtos.UserRequestDTO;
import com.example.Digital_Wallet_Api.user.dtos.UserResponseDTO;
import com.example.Digital_Wallet_Api.user.entities.User;
import com.example.Digital_Wallet_Api.user.entities.enums.Role;
import com.example.Digital_Wallet_Api.user.entities.enums.Status;
import com.example.Digital_Wallet_Api.user.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder ;

    @Mock
    private ModelMapper mapDto;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRequestDTO userRequestDTO;
    private User user;
    private  UserResponseDTO userResponseDTO;

    //---------------Arrange-------------------//
    @BeforeEach
    void setup (){
        userRequestDTO = new UserRequestDTO();
        userRequestDTO.setFirstName("Miracle");
        userRequestDTO.setLastName("Rapheal");
        userRequestDTO.setEmail("Mchikereuba@gmail.com");
        userRequestDTO.setPassword("miracle177!");



        user = User.builder()
                .userId(1L)
                .firstName("Miracle")
                .lastName("Rapheal")
                .email("Mchikereuba@gmail.com")
                .passwordHash(encoder.encode("miracle177!"))
                .role(Role.USER)
                .status(Status.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

       userResponseDTO = UserResponseDTO.builder()
                .id(1L)
                .firstName("Miracle")
                .lastName("Rapheal")
                .email("Mchikereuba@gmail.com")
                .role("user")
                .status("active")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

    }

    @Test
    void createUser_shouldReturnUserResponse_whenUserDoesNotExist(){

    //mock behaviors
        when(userRepository.existsByEmail(userRequestDTO.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);
       when(encoder.encode(any(String.class))).thenReturn("hashedPassword");
        when(mapDto.map(any(User.class),eq(UserResponseDTO.class))).thenReturn(userResponseDTO);

        //------------Act------//

        UserResponseDTO response = userService.createUser(userRequestDTO);

        //------assert------//

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Miracle", response.getFirstName());
        assertEquals("Rapheal", response.getLastName());
        assertEquals("Mchikereuba@gmail.com", response.getEmail());
        verify(userRepository, times(1)).save(any(User.class));


    }
    @Test
    void createUser_shouldThrowAnException_whenUserExists (){
        when(userRepository.existsByEmail(userRequestDTO.getEmail())).thenReturn(true);

        RuntimeException e = assertThrows(RuntimeException.class, ()-> userService.createUser(userRequestDTO));
        assertEquals("User already exists",e.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }



    @Test
    void testGetUserById_Success (){
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(mapDto.map(any(User.class), eq(UserResponseDTO.class))).thenReturn(userResponseDTO);

        UserResponseDTO response = userService.getUserById(1L);

        assertNotNull(response);
        assertEquals("Miracle", response.getFirstName());

    }

    @Test
    void testGetUserById_shouldFailIfUserDoesNotExist(){
        Long id = 2L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class,()-> userService.getUserById(id));

        assertEquals("User with user_id: " + id + " does not exist",e.getMessage());

    }


    @Test
    void testGetAllUsers(){
        User user1 = User.builder()
                .userId(1L)
                .firstName("Miracle")
                .lastName("Rapheal")
                .email("m1@example.com")
                .passwordHash("hashedPassword")
                .role(Role.USER)
                .status(Status.ACTIVE)
                .build();

        User user2 = User.builder()
                .userId(2L)
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .passwordHash("hashedPassword")
                .role(Role.USER)
                .status(Status.ACTIVE)
                .build();


        when(userRepository.findAll()).thenReturn(List.of(user1,user2));
        when(mapDto.map(any(User.class), eq(UserResponseDTO.class))).thenAnswer(inv->{
            User u = inv.getArgument(0);
            return UserResponseDTO.builder()
                    .firstName(u.getFirstName())
                    .email(u.getEmail())
                    .lastName(u.getLastName())
                    .createdAt(u.getCreatedAt())
                    .role(u.getRole().name())
                    .status(u.getStatus().name())
                    .updatedAt(u.getUpdatedAt())
                    .build();
        });

        List<UserResponseDTO> list = userService.getAllUsers();

        assertNotNull(list);
        assertEquals(2,list.size());
        verify(mapDto, times(2)).map(any(User.class), eq(UserResponseDTO.class));
    }




}


