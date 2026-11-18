package org.example.covoiturage_plateforme.Dto.Response;


import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class VehicleResponse {

    private UUID Id;

    private String brand;

    private String model;

    private String licensePlate;

    private String color;

    private Integer capacity;

    private UUID driverId;
}
