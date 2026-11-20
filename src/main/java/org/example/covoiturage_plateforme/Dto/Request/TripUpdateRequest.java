
package org.example.covoiturage_plateforme.Dto.Request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TripUpdateRequest {

    @NotBlank(message = "Departure city is required")
    private String departureCity;

    @NotBlank(message = "Arrival city is required")
    private String arrivalCity;

    @NotNull(message = "Departure date and time are required")
    @Future(message = "Departure date must be in the future")
    private LocalDateTime departureDateTime;

    @NotNull(message = "Available seats are required")
    @Min(value = 1, message = "Available seats must be at least 1")
    private Integer availableSeats;
}
