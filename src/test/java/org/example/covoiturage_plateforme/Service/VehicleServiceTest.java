
package org.example.covoiturage_plateforme.Service;

import org.example.covoiturage_plateforme.Dto.Request.VehicleCreateRequest;
import org.example.covoiturage_plateforme.Dto.Response.VehicleResponse;
import org.example.covoiturage_plateforme.Entity.Driver;
import org.example.covoiturage_plateforme.Entity.Passenger;
import org.example.covoiturage_plateforme.Entity.User;
import org.example.covoiturage_plateforme.Entity.Vehicle;
import org.example.covoiturage_plateforme.Exception.UserNotFoundException;
import org.example.covoiturage_plateforme.Exception.VehicleAlreadyExistException;
import org.example.covoiturage_plateforme.Exception.VehicleNotFoundException;
import org.example.covoiturage_plateforme.Mapper.VehicleMapper;
import org.example.covoiturage_plateforme.Repository.UserRepository;
import org.example.covoiturage_plateforme.Repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private VehicleMapper vehicleMapper;

    @InjectMocks
    private VehicleService vehicleService;

    @Test
    void createVehicle_success() {

        UUID driverId = UUID.randomUUID();

        Driver driver = new Driver(
                "Ahmed", "Alaoui", "ahmed@gmail.com",
                "0612345678", "ABC123"
        );

        VehicleCreateRequest request = new VehicleCreateRequest();
        request.setBrand("Toyota");
        request.setModel("Corolla");
        request.setLicensePlate("12345-A-6");
        request.setColor("White");
        request.setCapacity(5);

        VehicleResponse expectedResponse = new VehicleResponse();

        when(userRepository.findByIdAndActiveTrue(driverId))
                .thenReturn(Optional.of(driver));

        when(vehicleRepository.existsByLicensePlate("12345-A-6"))
                .thenReturn(false);

        when(vehicleRepository.save(any(Vehicle.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(vehicleMapper.toResponse(any(Vehicle.class)))
                .thenReturn(expectedResponse);

        VehicleResponse result = vehicleService.createVehicle(request, driverId);

        assertSame(expectedResponse, result);

        verify(userRepository).findByIdAndActiveTrue(driverId);
        verify(vehicleRepository).existsByLicensePlate("12345-A-6");
        verify(vehicleRepository).save(any(Vehicle.class));
        verify(vehicleMapper).toResponse(any(Vehicle.class));
    }

    @Test
    void createVehicle_driverNotFound() {

        UUID driverId = UUID.randomUUID();

        VehicleCreateRequest request = new VehicleCreateRequest();

        when(userRepository.findByIdAndActiveTrue(driverId))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> vehicleService.createVehicle(request, driverId));

        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }

    @Test
    void createVehicle_userIsNotDriver() {

        UUID driverId = UUID.randomUUID();

        Passenger passenger = new Passenger(
                "Ali", "Amrani", "ali@gmail.com", "0612345678"
        );

        VehicleCreateRequest request = new VehicleCreateRequest();

        when(userRepository.findByIdAndActiveTrue(driverId))
                .thenReturn(Optional.of(passenger));

        assertThrows(IllegalArgumentException.class,
                () -> vehicleService.createVehicle(request, driverId));

        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }

    @Test
    void getVehicleById_success() {

        UUID vehicleId = UUID.randomUUID();

        Vehicle vehicle = new Vehicle();
        VehicleResponse expectedResponse = new VehicleResponse();

        when(vehicleRepository.findById(vehicleId))
                .thenReturn(Optional.of(vehicle));

        when(vehicleMapper.toResponse(vehicle))
                .thenReturn(expectedResponse);

        VehicleResponse result = vehicleService.getVehicleById(vehicleId);

        assertSame(expectedResponse, result);

        verify(vehicleRepository).findById(vehicleId);
        verify(vehicleMapper).toResponse(vehicle);
    }

    @Test
    void getVehicleById_notFound() {

        UUID vehicleId = UUID.randomUUID();

        when(vehicleRepository.findById(vehicleId))
                .thenReturn(Optional.empty());

        assertThrows(VehicleNotFoundException.class,
                () -> vehicleService.getVehicleById(vehicleId));

        verify(vehicleRepository).findById(vehicleId);

        verify(vehicleMapper, never()).toResponse(any(Vehicle.class));
    }
}