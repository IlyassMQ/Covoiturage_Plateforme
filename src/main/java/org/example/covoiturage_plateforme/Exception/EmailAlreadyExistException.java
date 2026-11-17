package org.example.covoiturage_plateforme.Exception;

public class EmailAlreadyExistException extends RuntimeException {
    public EmailAlreadyExistException(String message) {
        super(message);
    }

    public EmailAlreadyExistException() {
        super("Email Already Exist");
    }
}
