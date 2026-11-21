package org.example.covoiturage_plateforme.Exception;

public class ReservationNotFoundException extends RuntimeException {
    public ReservationNotFoundException(String message) {
        super(message);
    }

    public ReservationNotFoundException() {
        super("Reservation Not Found");
    }
}
