package org.example.covoiturage_plateforme.Dto.Request;


import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleUpdateRequest {

    @NotBlank(message = "Brand is required")
    private String brand;

    @NotBlank(message = "Model is required")
    private String model;

    @NotBlank(message = "LicencePlate is required")
    private String licensePlate;


    @NotBlank(message = "Color is required")
    private String color;

    @NotNull(message = "Capacity is required")
    @Min(value = 1)
    @Max(value = 8,message = "Max capacity is 8")
    private Integer capacity;

}
