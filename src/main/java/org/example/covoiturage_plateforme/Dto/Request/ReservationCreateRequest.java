package org.example.covoiturage_plateforme.Dto.Request;



import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ReservationCreateRequest {

    @NotNull
    @Positive
    private Integer numberOfSeats;

    @NotNull
    private UUID tripId;

    @NotNull
    private UUID pickUpStopId;

    @NotNull
    private UUID dropOffStopId;
}
