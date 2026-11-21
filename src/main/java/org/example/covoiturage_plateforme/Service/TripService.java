package org.example.covoiturage_plateforme.Service;


import lombok.RequiredArgsConstructor;
import org.example.covoiturage_plateforme.Dto.Request.StopCreateRequest;
import org.example.covoiturage_plateforme.Dto.Request.TripCreateRequest;
import org.example.covoiturage_plateforme.Dto.Request.TripSearchRequest;
import org.example.covoiturage_plateforme.Dto.Response.TripResponse;
import org.example.covoiturage_plateforme.Entity.*;
import org.example.covoiturage_plateforme.Entity.Enum.ReservationStatus;
import org.example.covoiturage_plateforme.Entity.Enum.TripStatus;
import org.example.covoiturage_plateforme.Exception.BusinessRuleException;
import org.example.covoiturage_plateforme.Exception.TripNotFoundException;
import org.example.covoiturage_plateforme.Exception.UserNotFoundException;
import org.example.covoiturage_plateforme.Exception.VehicleNotFoundException;
import org.example.covoiturage_plateforme.Mapper.StopMapper;
import org.example.covoiturage_plateforme.Mapper.TripMapper;
import org.example.covoiturage_plateforme.Repository.TripRepository;
import org.example.covoiturage_plateforme.Repository.UserRepository;
import org.example.covoiturage_plateforme.Repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;

    private final TripMapper tripMapper;
    private final StopMapper stopMapper;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;


    @Transactional
    public TripResponse createTrip(TripCreateRequest request, UUID driverId){
        Driver driver = getDriver(driverId);
        Vehicle vehicle = getVehicle(request.getVehicleId());

       validateTripRequest(request,driver,vehicle);
       validateVehicleAvailability(vehicle);
       validateStops(request);

        Trip trip = new Trip(request.getDepartureCity(), request.getArrivalCity(),request.getDepartureDateTime(), request.getAvailableSeats(), TripStatus.PUBLISHED,vehicle,driver);

        List<Stop> stops = request.getStopsList().stream().map(stopMapper::toEntity).toList();

        for (Stop s : stops) {
            s.setTrip(trip);
            trip.getStops().add(s);
        }
        driver.getTrips().add(trip);
        vehicle.getTrips().add(trip);
        Trip savedTrip = tripRepository.save(trip);

        return tripMapper.toResponse(savedTrip);


    }

    private Driver getDriver(UUID driverId){

        User user = userRepository.findByIdAndActiveTrue(driverId)
                .orElseThrow(UserNotFoundException::new);

        if (!(user instanceof Driver driver)){
            throw new IllegalArgumentException("The user is not Driver");

        }
        return driver;
    }

    private Vehicle getVehicle(UUID vehicleId){
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(VehicleNotFoundException::new);
        return vehicle;
    }


    private void validateVehicleAvailability(Vehicle vehicle) {

        if (tripRepository.existsByVehicle_IdAndStatus(vehicle.getId(),TripStatus.PUBLISHED)) {
            throw new BusinessRuleException("Vehicle already have a published trip");
        }
    }

    private void validateTripRequest(TripCreateRequest request, Driver driver, Vehicle vehicle) {

        if (!(vehicle.getDriver().getId().equals(driver.getId()))) {
            throw new IllegalArgumentException("Only the Vehicle Owner who can create the trip");
        }

        LocalDateTime now = LocalDateTime.now();

        if (request.getDepartureDateTime().isBefore(now)) {
            throw new BusinessRuleException("The departure time must be in the future");
        }

        if (request.getAvailableSeats() < 1) {
            throw new BusinessRuleException("The available seats must be at least 1");
        }

        if (request.getAvailableSeats() > vehicle.getCapacity()) {
            throw new BusinessRuleException("The available seats is more then the vehicle capacity");
        }

        if (request.getDepartureCity().equalsIgnoreCase(request.getArrivalCity())) {
            throw new BusinessRuleException("The departure city is the arrival city");
        }


    }

    private void validateStops(TripCreateRequest request) {

        List<StopCreateRequest> stops = request.getStopsList();

        if (stops.size() < 2) {
            throw new BusinessRuleException("The stops must be at least 2");
        }

        Set<String> cities = new HashSet<>();

        for (StopCreateRequest stop : stops) {
            if (!cities.add(stop.getCity().toLowerCase().trim())) {
                throw new BusinessRuleException(
                        "A city cannot appear more than once in the trip"
                );
            }
        }
        if (stops.get(0).getStopOrder() != 0) {
            throw new BusinessRuleException("The first storp have to be order 0");
        }

        if (stops.get(0).getPriceFromStart().compareTo(BigDecimal.ZERO) > 0) {
            throw new BusinessRuleException("The price of the first Point have to be 0");
        }

        if (!(stops.get(0).getCity().equals(request.getDepartureCity()))) {
            throw new BusinessRuleException("The first stop city must match the trip departure city");
        }

        if (!(stops.get(stops.size() - 1).getCity().equals(request.getArrivalCity()))) {
            throw new BusinessRuleException("The last stop city must match the trip arrival city");
        }

        for (int i = 0; i < stops.size() - 1; i++) {

            if (stops.get(i).getStopOrder() + 1 != stops.get(i + 1).getStopOrder()) {
                throw new BusinessRuleException("Stop order must be ordered");
            }

            if (!(stops.get(i).getArrivalTime().isBefore(stops.get(i + 1).getArrivalTime()))) {
                throw new BusinessRuleException("Stop arrival times must be strictly increasing");
            }

            if (!(stops.get(i).getPriceFromStart().compareTo(stops.get(i + 1).getPriceFromStart()) < 0)) {
                throw new BusinessRuleException("Stop prices must be strictly increasing");
            }
        }
    }


    public List<TripResponse> searchTrips(TripSearchRequest request) {

        LocalDateTime startDate = request.getDepartureDate().atStartOfDay();

        LocalDateTime endDate = request.getArrivalDate().plusDays(1).atStartOfDay();

        List<TripResponse> searchedTrips =  tripRepository.searchTrips(request.getDepartureCity(), request.getArrivalCity(), startDate, endDate, LocalDateTime.now(),request.getNumberOfSeats(),request.getMaxPrice())
                .stream()
                .map(tripMapper::toResponse)
                .toList();

        return searchedTrips;
    }


    @Transactional
    public void completeTrip(UUID tripId, UUID driverId) {

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(TripNotFoundException::new);

        if (!trip.getDriver().getId().equals(driverId)) {
            throw new IllegalArgumentException("Only the Driver can complete the trip");
        }

        if (trip.getStatus() == TripStatus.CANCELED ||
                trip.getStatus() == TripStatus.COMPLETED) {
            throw new BusinessRuleException("This trip cannot be completed");
        }


        if (trip.getDepartureDateTime().isAfter(LocalDateTime.now())) {
            throw new BusinessRuleException("The trip cannot be completed before its departure time");
        }

        trip.setStatus(TripStatus.COMPLETED);
    }

    @Transactional
    public void cancelTrip(UUID tripId, UUID driverId) {

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(TripNotFoundException::new);

        if (!trip.getDriver().getId().equals(driverId)) {
            throw new IllegalArgumentException("Only the Driver Owner who can cancel the trip");
        }

        if (trip.getStatus() == TripStatus.CANCELED || trip.getStatus() == TripStatus.COMPLETED) {
            throw new BusinessRuleException("This trip cannot be canceled");
        }

        trip.setStatus(TripStatus.CANCELED);

        for (Reservation reservation : trip.getReservations()) {
            if (reservation.getStatus() == ReservationStatus.CONFIRMED) {
                reservation.setStatus(ReservationStatus.CANCELED);
            }
        }
    }



}
