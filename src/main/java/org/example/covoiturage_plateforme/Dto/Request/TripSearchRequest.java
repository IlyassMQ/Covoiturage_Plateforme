
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

    @NotNull
    @FutureOrPresent
    private LocalDate departureDate;

    @NotNull
    @Future
    private LocalDate arrivalDate;

    @NotNull
    @Positive
    private BigDecimal maxPrice;

    @NotNull
    @Min(1)
    private Integer numberOfSeats;
}
