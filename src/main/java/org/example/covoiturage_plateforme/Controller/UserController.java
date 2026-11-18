package org.example.covoiturage_plateforme.Controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.covoiturage_plateforme.Dto.Request.UserCreateRequest;
import org.example.covoiturage_plateforme.Dto.Request.UserUpdateRequest;
import org.example.covoiturage_plateforme.Dto.Response.UserResponse;
import org.example.covoiturage_plateforme.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping("/create-driver")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createDriver(@Valid @RequestBody UserCreateRequest request){
      return    userService.createDriver(request);
    }

    @PostMapping("/create-passanger")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createPassenger(@Valid @RequestBody UserCreateRequest request){
        return   userService.createPassenger(request);
    }

    @PostMapping("/create-admin")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createAdmin(@Valid @RequestBody UserCreateRequest request){
        return   userService.createAdmin(request);
    }


    @GetMapping("/email")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getUserByEmail(@RequestParam("email") String email){
        return userService.getUserByEmail(email);
    }

    @GetMapping("/lastname")
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponse> getUserByLastName(@RequestParam("lastname") String lastName){
            return userService.getUserByLastName(lastName);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponse> getAllActiveUser(){
        return userService.getAllUsers();
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse updateUser(@Valid @RequestBody UserUpdateRequest request, @PathVariable("id") UUID id){
        return userService.updateUser(request, id);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse deleteUser(@PathVariable("id") UUID id){
        return userService.deleteUser(id);
    }

    @GetMapping("/me/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse userProfil(@PathVariable("userId")UUID userId){
        return userService.userProfil(userId);
    }


}
