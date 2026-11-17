package org.example.covoiturage_plateforme.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "stops")
@Getter
@Setter
@NoArgsConstructor
public class Stop {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String city;

    @Column(name = "stop_order", nullable = false)
    private Integer stopOrder;

    @Column(nullable = false)
    private LocalDateTime arrivalTime;

    @Column(nullable = false)
    private BigDecimal priceFromStart;

    @ManyToOne(optional = false)
    @JoinColumn(name = "trip_id",nullable = false)
    private Trip trip;

    public Stop(String city, Integer stopOrder, LocalDateTime arrivalTime, BigDecimal priceFromStart, Trip trip) {
        this.city = city;
        this.stopOrder = stopOrder;
        this.arrivalTime = arrivalTime;
        this.priceFromStart = priceFromStart;
        this.trip = trip;
    }
}