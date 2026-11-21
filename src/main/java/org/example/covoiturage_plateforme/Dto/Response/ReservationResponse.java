package org.example.covoiturage_plateforme.Dto.Response;

import lombok.Getter;
import lombok.Setter;
import org.example.covoiturage_plateforme.Entity.Enum.ReservationStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
public class ReservationResponse {

    private UUID id;

    private Integer numberOfSeats;

    private BigDecimal totalAmount;

    private LocalDateTime reservationDate;

    private ReservationStatus status;

    private UUID passengerId;

    private UUID tripId;

    private StopResponse pickupStop;

    private StopResponse dropoffStop;


}
