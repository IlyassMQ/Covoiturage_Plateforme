package org.example.covoiturage_plateforme.Dto.Request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateRequest {

    @NotBlank(message = "First Name is required")
    private String firstName;

    @NotBlank(message = "Last Name is required")
    private String lastName;


    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valide")
    private String email;

    @NotNull
    @Size(max = 10 ,min = 8)
    private String phone ;

    
    private String licence_number;


}
