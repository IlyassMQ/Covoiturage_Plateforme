package org.example.covoiturage_plateforme.Mapper;
import org.example.covoiturage_plateforme.Dto.Request.UserCreateRequest;
import org.example.covoiturage_plateforme.Dto.Response.UserResponse;
import org.example.covoiturage_plateforme.Entity.Admin;
import org.example.covoiturage_plateforme.Entity.Driver;
import org.example.covoiturage_plateforme.Entity.Passenger;
import org.example.covoiturage_plateforme.Entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.SubclassMapping;


@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "licenseNumber",target = "license_number")
    UserResponse toResponse(Driver driver);
    UserResponse toResponse(Passenger passenger);
    UserResponse toResponse(Admin admin);

    default UserResponse toResponse(User user) {
        if (user instanceof Driver driver) {
            return toResponse(driver);
        }
        if (user instanceof Passenger passenger) {
            return toResponse(passenger);
        }
        if (user instanceof Admin admin) {
            return toResponse(admin);
        }
        throw new IllegalArgumentException("Unknown user type");
    }


    Driver toDriver(UserCreateRequest userCreateRequest);
    Passenger toPassenger(UserCreateRequest userCreateRequest);
    Admin toAdmin(UserCreateRequest userCreateRequest);

}
