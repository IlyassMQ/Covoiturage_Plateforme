
package org.example.covoiturage_plateforme.Dto.Response;

import lombok.Getter;
import lombok.Setter;
import org.example.covoiturage_plateforme.Entity.Enum.TripStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class TripResponse{

    private UUID id;

    private String departureCity;

    private String arrivalCity;

    private LocalDateTime departureDateTime;

    private Integer availableSeats;

    private TripStatus status;

    private UUID driverId;

    private UUID vehicleId;

    private List<StopResponse> stops;
}
