package org.example.covoiturage_plateforme.Service;

import org.example.covoiturage_plateforme.Dto.Request.UserCreateRequest;
import org.example.covoiturage_plateforme.Dto.Response.UserResponse;
import org.example.covoiturage_plateforme.Entity.Driver;
import org.example.covoiturage_plateforme.Exception.EmailAlreadyExistException;
import org.example.covoiturage_plateforme.Mapper.UserMapper;
import org.example.covoiturage_plateforme.Repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;


    @Test
    void createDriver_success() {

        UserCreateRequest request = new UserCreateRequest();
        request.setFirstName("Ahmed");
        request.setLastName("Alaoui");
        request.setEmail("ahmed@gmail.com");
        request.setPhone("0612345678");
        request.setLicence_number("ABC123");

        UserResponse expectedResponse = new UserResponse();

        when(userRepository.existsByEmail("ahmed@gmail.com"))
                .thenReturn(false);

        when(userRepository.save(any(Driver.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(userMapper.toResponse(any(Driver.class)))
                .thenReturn(expectedResponse);

        UserResponse result = userService.createDriver(request);

        assertSame(expectedResponse, result);

        verify(userRepository).existsByEmail("ahmed@gmail.com");
        verify(userRepository).save(any(Driver.class));
        verify(userMapper).toResponse(any(Driver.class));
    }


    @Test
    void createDriver_emailAlreadyExists() {

        UserCreateRequest request = new UserCreateRequest();
        request.setFirstName("Ahmed");
        request.setLastName("Alaoui");
        request.setEmail("ahmed@gmail.com");
        request.setPhone("0612345678");
        request.setLicence_number("ABC123");

        when(userRepository.existsByEmail("ahmed@gmail.com"))
                .thenReturn(true);



        assertThrows(
                EmailAlreadyExistException.class,
                () -> userService.createDriver(request)
        );

        verify(userRepository).existsByEmail("ahmed@gmail.com");


        verify(userRepository, never()).save(any(Driver.class));
    }
}
