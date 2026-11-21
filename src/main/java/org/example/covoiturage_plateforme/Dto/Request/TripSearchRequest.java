
package org.example.covoiturage_plateforme.Dto.Request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class TripSearchRequest {

    @NotBlank
    private String departureCity;

    @NotBlank
    private String arrivalCity;


    private LocalDate departureDate;


    private LocalDate arrivalDate;


    @Positive
    private BigDecimal maxPrice;

    
    @Min(1)
    private Integer numberOfSeats;
}
