package org.example.covoiturage_plateforme.Repository;

import org.example.covoiturage_plateforme.Entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    List<Reservation> findAllByPassenger_Id(UUID passengerId);

    List<Reservation> findAllByTrip_Id(UUID tripId);
}
