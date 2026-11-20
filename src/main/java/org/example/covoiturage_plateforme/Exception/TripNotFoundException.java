package org.example.covoiturage_plateforme.Exception;

public class TripNotFoundException extends RuntimeException {
    public TripNotFoundException(String message) {
        super(message);
    }

    public TripNotFoundException() {
        super("Trip Not Found Exception");
    }

}
