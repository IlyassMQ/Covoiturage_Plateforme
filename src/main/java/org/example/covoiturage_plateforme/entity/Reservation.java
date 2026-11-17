package org.example.covoiturage_plateforme.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.covoiturage_plateforme.Entity.Enum.ReservationStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reservations")
@Getter
@Setter
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private Integer numberOfSeats;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private LocalDateTime reservationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    @ManyToOne(optional = false)
    @JoinColumn(name = "passenger_id",nullable = false)
    private Passenger passenger;

    @ManyToOne(optional = false)
    @JoinColumn(name = "trip_id",nullable = false)
    private Trip trip;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pickup_stop_id",nullable = false)
    private Stop pickupStop;

    @ManyToOne(optional = false)
    @JoinColumn(name = "dropoff_stop_id",nullable = false)
    private Stop dropoffStop;


    public Reservation(Integer numberOfSeats, BigDecimal totalAmount, LocalDateTime reservationDate, ReservationStatus status, Passenger passenger, Trip trip, Stop pickupStop, Stop dropoffStop) {
        this.numberOfSeats = numberOfSeats;
        this.totalAmount = totalAmount;
        this.reservationDate = reservationDate;
        this.status = status;
        this.passenger = passenger;
        this.trip = trip;
        this.pickupStop = pickupStop;
        this.dropoffStop = dropoffStop;
    }
}