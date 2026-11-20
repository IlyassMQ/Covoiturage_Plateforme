package org.example.covoiturage_plateforme.Repository;

import org.example.covoiturage_plateforme.Entity.Enum.TripStatus;
import org.example.covoiturage_plateforme.Entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface TripRepository extends JpaRepository<Trip, UUID> {
    boolean existsByVehicle_IdAndStatus(UUID vehicleId, TripStatus status);


    @Query("""
         select distinct t from Trip t
         join Stop departureStop on departureStop.trip.id = t.id
         join Stop arrivalStop on arrivalStop.trip.id = t.id
            where departureStop.city = :departureCity
              and arrivalStop.city = :arrivalCity
              and departureStop.stopOrder < arrivalStop.stopOrder
              and t.departureDateTime >= :startDate
              and t.departureDateTime < :endDate
              and t.departureDateTime >= :now
              and t.status = TripStatus.PUBLISHED
              and t.availableSeats >= :numberOfSeats
              and (arrivalStop.priceFromStart - departureStop.priceFromStart) <= :maxPrice
            order by t.departureDateTime ASC

""")
    List<Trip> searchTrips(@Param("departureCity") String departureCity, @Param("arrivalCity") String arrivalCity,
                           @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate,
                           @Param("now") LocalDateTime now,
                           @Param("numberOfSeats") Integer numberOfSeats,
                           @Param("maxPrice")BigDecimal maxPrice);


}
