package org.example.covoiturage_plateforme.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.covoiturage_plateforme.Entity.Enum.TripStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "trips")
@Getter
@Setter
@NoArgsConstructor
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String departureCity;

    @Column(nullable = false)
    private String arrivalCity;

    @Column(nullable = false)
    private LocalDateTime departureDateTime;

    @Column(nullable = false)
    private Integer availableSeats;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TripStatus status;

    @ManyToOne(optional = false)
    @JoinColumn(name = "vehicle_id",nullable = false)
    private Vehicle vehicle;

    @ManyToOne(optional = false)
    @JoinColumn(name = "driver_id",nullable = false)
    private Driver driver;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Stop> stops = new ArrayList<>();

    @OneToMany(mappedBy = "trip")
    private List<Reservation> reservations = new ArrayList<>();

    public Trip(String departureCity, String arrivalCity, LocalDateTime departureDateTime, Integer availableSeats, TripStatus status, Vehicle vehicle, Driver driver) {
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
        this.departureDateTime = departureDateTime;
        this.availableSeats = availableSeats;
        this.status = status;
        this.vehicle = vehicle;
        this.driver = driver;
    }
}