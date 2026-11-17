package org.example.covoiturage_plateforme.Service;


import lombok.RequiredArgsConstructor;
import org.example.covoiturage_plateforme.Dto.Request.UserCreateRequest;
import org.example.covoiturage_plateforme.Dto.Request.UserUpdateRequest;
import org.example.covoiturage_plateforme.Dto.Response.UserResponse;
import org.example.covoiturage_plateforme.Exception.EmailAlreadyExistException;
import org.example.covoiturage_plateforme.Exception.UserNotFoundException;
import org.example.covoiturage_plateforme.Mapper.UserMapper;
import org.example.covoiturage_plateforme.Repository.UserRepository;
import org.example.covoiturage_plateforme.Entity.Admin;
import org.example.covoiturage_plateforme.Entity.Driver;
import org.example.covoiturage_plateforme.Entity.Passenger;
import org.example.covoiturage_plateforme.Entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class UserService {

        private final UserRepository userRepository;

        private final UserMapper userMapper;

        @Transactional

        public UserResponse saveUser(User user){
            if (userRepository.existsByEmail(user.getEmail())){
                throw new EmailAlreadyExistException();
            }

            User savedUser = userRepository.save(user);

            if (savedUser instanceof Driver driver){
                return userMapper.toResponse(driver);
            }

            if (savedUser instanceof Passenger passenger){
                return userMapper.toResponse(passenger);
            }
            if (savedUser instanceof Admin admin){
                return userMapper.toResponse(admin);
            }

            return userMapper.toResponse(savedUser);
        }

        @Transactional
        public UserResponse createDriver(UserCreateRequest request){
                if (request.getLicence_number().isBlank()){
                    throw new IllegalArgumentException("Licence number is required");
                }
                Driver driver = new Driver(request.getFirstName(), request.getLastName(), request.getEmail(), request.getPhone(), request.getLicence_number());

                return saveUser(driver);
        }

    @Transactional
    public UserResponse createPassenger(UserCreateRequest request){

        Passenger passenger = new Passenger(request.getFirstName(), request.getLastName(), request.getEmail(), request.getPhone());
        return saveUser(passenger);
    }

    @Transactional
    public UserResponse createAdmin(UserCreateRequest request){

        Admin admin = new Admin(request.getFirstName(), request.getLastName(), request.getEmail(), request.getPhone());
        return saveUser(admin);
    }


    public UserResponse getUserByEmail(String email){
            User user = userRepository.findByEmailAnd_ActiveTrue(email)
                    .orElseThrow(UserNotFoundException::new);
            return userMapper.toResponse(user);

    }

    public List<UserResponse> getUserByLastName(String lastName){

            List<User> userList = userRepository.findAllByLastNameAnd_ActiveTrue(lastName);
             List<UserResponse> responseList = userList.stream().map(userMapper::toResponse).toList();


            return responseList;
    }

    public List<UserResponse> getAllUsers(){
            List<User> userList = userRepository.findAllBy_ActiveTrue();

           List<UserResponse> responseList =  userList.stream().map(userMapper::toResponse).toList();

            return responseList;
    }


    @Transactional
    public UserResponse updateUser(UserUpdateRequest request,UUID userId){
            User user = userRepository.findByIdAnd_ActiveTrue(userId)
                    .orElseThrow(UserNotFoundException::new);


            if (userRepository.existsByEmailAndIdNot(request.getEmail(), userId)){
                throw new EmailAlreadyExistException();
            }

            user.setFirstName(request.getFirstName());

            user.setLastName(request.getLastName());

            user.setEmail(request.getEmail());

            if (user instanceof Driver driver){
                if (!driver.getLicenseNumber().equals(request.getLicence_number())) {
                    driver.setLicenseNumber(request.getLicence_number());
                }
            }

            userRepository.save(user);

            return userMapper.toResponse(user);

    }

    @Transactional
    public UserResponse deleteUser(UUID id){
            User user = userRepository.findByIdAnd_ActiveTrue(id).
                    orElseThrow(UserNotFoundException::new);

            user.set_Active(false);
            userRepository.save(user);

            return userMapper.toResponse(user);


    }

}
