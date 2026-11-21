package org.example.covoiturage_plateforme.Service;

import org.example.covoiturage_plateforme.Dto.Request.ReservationCreateRequest;
import org.example.covoiturage_plateforme.Dto.Response.ReservationResponse;
import org.example.covoiturage_plateforme.Entity.*;
import org.example.covoiturage_plateforme.Entity.Enum.TripStatus;
import org.example.covoiturage_plateforme.Exception.BusinessRuleException;
import org.example.covoiturage_plateforme.Exception.TripNotFoundException;
import org.example.covoiturage_plateforme.Exception.UserNotFoundException;
import org.example.covoiturage_plateforme.Mapper.ReservationMapper;
import org.example.covoiturage_plateforme.Repository.ReservationRepository;
import org.example.covoiturage_plateforme.Repository.TripRepository;
import org.example.covoiturage_plateforme.Repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TripRepository tripRepository;

    @Mock
    private ReservationMapper reservationMapper;

    @InjectMocks
    private ReservationService reservationService;


    private Passenger createPassenger() {
        Passenger passenger = new Passenger();
        passenger.setId(UUID.randomUUID());
        passenger.setReservations(new ArrayList<>());
        return passenger;
    }

    private Stop createStop(UUID id, int order, String price) {
        Stop stop = new Stop();
        stop.setId(id);
        stop.setStopOrder(order);
        stop.setPriceFromStart(new BigDecimal(price));
        return stop;
    }

    private Trip createTrip(Stop pickupStop, Stop dropOffStop) {
        Trip trip = new Trip();
        trip.setId(UUID.randomUUID());
        trip.setAvailableSeats(5);
        trip.setStatus(TripStatus.PUBLISHED);
        trip.setStops(new ArrayList<>(List.of(pickupStop, dropOffStop)));
        trip.setReservations(new ArrayList<>());
        return trip;
    }

    private ReservationCreateRequest createRequest(UUID tripId, UUID pickupStopId, UUID dropOffStopId) {

        ReservationCreateRequest request = new ReservationCreateRequest();
        request.setNumberOfSeats(2);
        request.setTripId(tripId);
        request.setPickUpStopId(pickupStopId);
        request.setDropOffStopId(dropOffStopId);

        return request;
    }


    @Test
    void createReservation_success() {

        UUID passengerId = UUID.randomUUID();

        Passenger passenger = createPassenger();

        Stop pickupStop = createStop(UUID.randomUUID(), 0, "0");
        Stop dropOffStop = createStop(UUID.randomUUID(), 1, "60");

        Trip trip = createTrip(pickupStop, dropOffStop);

        ReservationCreateRequest request =
                createRequest(trip.getId(), pickupStop.getId(), dropOffStop.getId());

        ReservationResponse expectedResponse = new ReservationResponse();

        when(userRepository.findByIdAndActiveTrue(passengerId))
                .thenReturn(Optional.of(passenger));

        when(tripRepository.findByIdAndStatus(trip.getId(), TripStatus.PUBLISHED))
                .thenReturn(Optional.of(trip));

        when(reservationRepository.save(any(Reservation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(reservationMapper.toResponse(any(Reservation.class)))
                .thenReturn(expectedResponse);

        ReservationResponse result =
                reservationService.createReservation(request, passengerId);

        assertSame(expectedResponse, result);

        assertEquals(3, trip.getAvailableSeats());

        assertEquals(1, passenger.getReservations().size());
        assertEquals(1, trip.getReservations().size());

        verify(reservationRepository).save(any(Reservation.class));
        verify(reservationMapper).toResponse(any(Reservation.class));
    }


    @Test
    void createReservation_passengerNotFound() {

        UUID passengerId = UUID.randomUUID();

        ReservationCreateRequest request = new ReservationCreateRequest();

        when(userRepository.findByIdAndActiveTrue(passengerId))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> reservationService.createReservation(request, passengerId));

        verify(tripRepository, never())
                .findByIdAndStatus(any(), any());

        verify(reservationRepository, never()).save(any());
    }



    @Test
    void createReservation_tripNotFound() {

        UUID passengerId = UUID.randomUUID();
        UUID tripId = UUID.randomUUID();

        Passenger passenger = createPassenger();

        ReservationCreateRequest request =
                createRequest(tripId, UUID.randomUUID(), UUID.randomUUID());

        when(userRepository.findByIdAndActiveTrue(passengerId))
                .thenReturn(Optional.of(passenger));

        when(tripRepository.findByIdAndStatus(tripId, TripStatus.PUBLISHED))
                .thenReturn(Optional.empty());

        assertThrows(TripNotFoundException.class,
                () -> reservationService.createReservation(request, passengerId));

        verify(reservationRepository, never()).save(any());
    }


    @Test
    void createReservation_notEnoughAvailableSeats() {

        UUID passengerId = UUID.randomUUID();

        Passenger passenger = createPassenger();

        Stop pickupStop = createStop(UUID.randomUUID(), 0, "0");
        Stop dropOffStop = createStop(UUID.randomUUID(), 1, "60");

        Trip trip = createTrip(pickupStop, dropOffStop);
        trip.setAvailableSeats(1);

        ReservationCreateRequest request =
                createRequest(trip.getId(), pickupStop.getId(), dropOffStop.getId());

        when(userRepository.findByIdAndActiveTrue(passengerId))
                .thenReturn(Optional.of(passenger));

        when(tripRepository.findByIdAndStatus(trip.getId(), TripStatus.PUBLISHED))
                .thenReturn(Optional.of(trip));

        assertThrows(BusinessRuleException.class,
                () -> reservationService.createReservation(request, passengerId));

        assertEquals(1, trip.getAvailableSeats());

        verify(reservationRepository, never()).save(any());
    }
}
