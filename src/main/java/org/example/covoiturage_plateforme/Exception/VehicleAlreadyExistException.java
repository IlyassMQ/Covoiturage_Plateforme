package org.example.covoiturage_plateforme.Exception;

public class VehicleAlreadyExistException extends RuntimeException {
    public VehicleAlreadyExistException(String message) {
        super(message);
    }

    public VehicleAlreadyExistException() {
        super("Vehicle Already Exist");
    }
}
