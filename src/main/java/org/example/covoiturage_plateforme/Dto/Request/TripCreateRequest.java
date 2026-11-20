
package org.example.covoiturage_plateforme.Dto.Request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class TripCreateRequest {

    @NotBlank(message = "Departure city is required")
    private String departureCity;

    @NotBlank(message = "Arrival city is required")
    private String arrivalCity;

    @NotNull(message = "Departure date and time are required")
    @Future(message = "Departure date must be in the future")
    private LocalDateTime departureDateTime;

    @NotNull(message = "Available seats are required")
    @Min(value = 1, message = "Available seat must be at least 1")
    private Integer availableSeats;

    @NotNull(message = "Vehicle ID is required")
    private UUID vehicleId;

    @NotNull
    @Size(min = 2)
    private List<StopCreateRequest> stopsList;
}
