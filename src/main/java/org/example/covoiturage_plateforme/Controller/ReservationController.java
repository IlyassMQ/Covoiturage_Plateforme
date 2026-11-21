package org.example.covoiturage_plateforme.Controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.covoiturage_plateforme.Dto.Request.ReservationCreateRequest;
import org.example.covoiturage_plateforme.Dto.Response.ReservationResponse;
import org.example.covoiturage_plateforme.Service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;



    @PostMapping("/create/{passengerId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse createReservation(@Valid @RequestBody ReservationCreateRequest request, @PathVariable UUID passengerId){
        return reservationService.createReservation(request, passengerId);
    }

    @PatchMapping("/cancel/{passengerId}/{reservationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelReservation(@PathVariable UUID passengerId, @PathVariable UUID reservationId) {
        reservationService.cancelReservation(reservationId, passengerId);
    }

    @GetMapping("/{reservationId}")
    @ResponseStatus(HttpStatus.OK)
    public ReservationResponse getReservationById(@PathVariable UUID reservationId) {
        return reservationService.getReservationById(reservationId);
    }

    @GetMapping("/passenger/{passengerId}")
    @ResponseStatus(HttpStatus.OK)
    public List<ReservationResponse> getPassengerReservations(@PathVariable UUID passengerId) {
        return reservationService.findAllByPassengerId(passengerId);
    }

    @GetMapping("/trip/{tripId}")
    @ResponseStatus(HttpStatus.OK)
    public List<ReservationResponse> getTripReservations(
            @PathVariable UUID tripId) {
        return reservationService.findAllByTripId(tripId);
    }

}
