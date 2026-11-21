package org.example.covoiturage_plateforme.Controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.covoiturage_plateforme.Dto.Request.TripCreateRequest;
import org.example.covoiturage_plateforme.Dto.Request.TripSearchRequest;
import org.example.covoiturage_plateforme.Dto.Response.TripResponse;
import org.example.covoiturage_plateforme.Service.TripService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/trip")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    @PostMapping("/create/{driverId}")
    @ResponseStatus(HttpStatus.CREATED)
    public TripResponse createTrip(@Valid @RequestBody TripCreateRequest request, @PathVariable("driverId") UUID driverId){
        return tripService.createTrip(request,driverId);

    }



    @PostMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<TripResponse> searchTrips(@Valid @RequestBody TripSearchRequest request) {
        return tripService.searchTrips(request);
    }

    @PutMapping("/cancel/{driverId}/{tripId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelTrip(@PathVariable UUID driverId, @PathVariable UUID tripId) {
        tripService.cancelTrip(tripId, driverId);
    }

    @PutMapping("complete/{driverId}/{tripId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void completeTrip(@PathVariable UUID driverId, @PathVariable UUID tripId){
        tripService.completeTrip(tripId,driverId);
    }

}
