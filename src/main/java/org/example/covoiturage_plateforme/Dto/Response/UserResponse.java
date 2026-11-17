package org.example.covoiturage_plateforme.Dto.Response;


import lombok.Getter;

import java.util.UUID;

@Getter
public class UserResponse {


    private UUID id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone ;

    private boolean is_Active;
    
    private String licence_number;

}
