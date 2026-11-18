package org.example.covoiturage_plateforme.Exception;

public class VehicleNotFoundException extends RuntimeException {
    public VehicleNotFoundException(String message) {
        super(message);
    }

    public VehicleNotFoundException() {
        super("Vehicle Not found");
    }
}
