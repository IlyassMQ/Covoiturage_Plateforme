package org.example.covoiturage_plateforme.Dto.Response;


import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserResponse {


    private UUID id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone ;

    private boolean active;

    private String license_number;

}
