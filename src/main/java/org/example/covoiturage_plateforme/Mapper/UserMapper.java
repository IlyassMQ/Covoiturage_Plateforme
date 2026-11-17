package org.example.covoiturage_plateforme.Mapper;
import org.example.covoiturage_plateforme.Dto.Request.UserCreateRequest;
import org.example.covoiturage_plateforme.Dto.Response.UserResponse;
import org.example.covoiturage_plateforme.Entity.Admin;
import org.example.covoiturage_plateforme.Entity.Driver;
import org.example.covoiturage_plateforme.Entity.Passenger;
import org.example.covoiturage_plateforme.Entity.User;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(Driver driver);
    UserResponse toResponse(Passenger passenger);
    UserResponse toResponse(Admin admin);

    UserResponse toResponse(User user);


    Driver toDriver(UserCreateRequest userCreateRequest);
    Passenger toPassenger(UserCreateRequest userCreateRequest);
    Admin toAdmin(UserCreateRequest userCreateRequest);

}
