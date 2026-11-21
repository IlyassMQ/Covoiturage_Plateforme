package org.example.covoiturage_plateforme.Service;


import lombok.RequiredArgsConstructor;
import org.example.covoiturage_plateforme.Dto.Request.ReservationCreateRequest;
import org.example.covoiturage_plateforme.Dto.Response.ReservationResponse;
import org.example.covoiturage_plateforme.Entity.*;
import org.example.covoiturage_plateforme.Entity.Enum.ReservationStatus;
import org.example.covoiturage_plateforme.Entity.Enum.TripStatus;
import org.example.covoiturage_plateforme.Exception.BusinessRuleException;
import org.example.covoiturage_plateforme.Exception.ReservationNotFoundException;
import org.example.covoiturage_plateforme.Exception.TripNotFoundException;
import org.example.covoiturage_plateforme.Exception.UserNotFoundException;
import org.example.covoiturage_plateforme.Mapper.ReservationMapper;
import org.example.covoiturage_plateforme.Repository.ReservationRepository;
import org.example.covoiturage_plateforme.Repository.TripRepository;
import org.example.covoiturage_plateforme.Repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final TripRepository tripRepository;
    private final ReservationMapper reservationMapper;

    @Transactional
    public ReservationResponse createReservation(ReservationCreateRequest request, UUID passengerId){
        Passenger passenger = getPassenger(passengerId);
        Trip trip = getTrip(request.getTripId());

        Stop pickupStop = trip.getStops().stream()
                .filter(stop1 -> stop1.getId().equals(request.getPickUpStopId())).findFirst()
                .orElseThrow(()->new BusinessRuleException("Stops dont match"));

        Stop dropOffStop = trip.getStops().stream()
                .filter(stop1 -> stop1.getId().equals(request.getDropOffStopId())).findFirst()
                .orElseThrow(()->new BusinessRuleException("Stops dont match"));



        if (pickupStop.getStopOrder() >= dropOffStop.getStopOrder()){
            throw new BusinessRuleException("the stops order failed");
        }

        if (request.getNumberOfSeats() > trip.getAvailableSeats()){
            throw new BusinessRuleException("Not enough available seats for this trip");
        }

        BigDecimal totalPrice = calculePrice(dropOffStop.getPriceFromStart(),pickupStop.getPriceFromStart(),request.getNumberOfSeats());


        Reservation reservation = new Reservation(request.getNumberOfSeats(),totalPrice, LocalDateTime.now(), ReservationStatus.CONFIRMED,passenger,trip,pickupStop,dropOffStop);

        int newAvailableSeat =  trip.getAvailableSeats() - request.getNumberOfSeats();
        trip.setAvailableSeats(newAvailableSeat);

        if (trip.getAvailableSeats() == 0){
            trip.setStatus(TripStatus.FULL);
        }

        passenger.getReservations().add(reservation);
        trip.getReservations().add(reservation);

        Reservation savedReservation = reservationRepository.save(reservation);

        return reservationMapper.toResponse(savedReservation);
        

    }

    private Passenger getPassenger(UUID passengerId) {

        User user = userRepository.findByIdAndActiveTrue(passengerId)
                .orElseThrow(UserNotFoundException::new);

        if (!(user instanceof Passenger passenger)) {
            throw new IllegalArgumentException("The user is not Passenger");

        }
        return passenger;
    }

    private Trip getTrip(UUID tripId){
        Trip trip = tripRepository.findByIdAndStatus(tripId,TripStatus.PUBLISHED)
                .orElseThrow(TripNotFoundException::new);

        return trip;

    }

    private BigDecimal calculePrice(BigDecimal dropOffPrice, BigDecimal pickUpPrice , int numberOfSeat){

        BigDecimal tripPrice = dropOffPrice.subtract(pickUpPrice);

        return tripPrice.multiply(BigDecimal.valueOf(numberOfSeat));
    }


    @Transactional
    public void cancelReservation(UUID reservationId,UUID passengerId ){
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(ReservationNotFoundException::new);

        if (!(reservation.getPassenger().getId().equals(passengerId))){
            throw new BusinessRuleException("you cant cancel this reservation");
        }
        if (!(reservation.getStatus().equals(ReservationStatus.CONFIRMED))){
            throw new BusinessRuleException("The reservation is not confirmed");
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime departureDate = reservation.getTrip().getDepartureDateTime();

        if (departureDate.isBefore(now.plusHours(24))) {
            throw new BusinessRuleException("Reservation can only be canceled at least 24 hours before departure");
        }

        reservation.setStatus(ReservationStatus.CANCELED);
        Trip trip = reservation.getTrip();

        int newAvailableSeat = trip.getAvailableSeats() + reservation.getNumberOfSeats();
        trip.setAvailableSeats(newAvailableSeat);

        if (trip.getStatus() == TripStatus.FULL ){
            trip.setStatus(TripStatus.PUBLISHED);
        }


    }

    public ReservationResponse getReservationById(UUID reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(ReservationNotFoundException::new);

        return reservationMapper.toResponse(reservation);
    }


    public List<ReservationResponse> findAllByPassengerId(UUID passengerId){
        getPassenger(passengerId);
        List<Reservation> passengerReservations= reservationRepository.findAllByPassenger_Id(passengerId);

        return passengerReservations.stream().map(reservationMapper::toResponse).toList();
    }

    public List<ReservationResponse> findAllByTripId(UUID tripId){
        tripRepository.findById(tripId)
                .orElseThrow(TripNotFoundException::new);
        
        List<Reservation> passengerReservations= reservationRepository.findAllByTrip_Id(tripId);

        return passengerReservations.stream().map(reservationMapper::toResponse).toList();
    }





}
