
package org.example.covoiturage_plateforme.Service;

import org.example.covoiturage_plateforme.Dto.Request.StopCreateRequest;
import org.example.covoiturage_plateforme.Dto.Request.TripCreateRequest;
import org.example.covoiturage_plateforme.Dto.Response.TripResponse;
import org.example.covoiturage_plateforme.Entity.*;
import org.example.covoiturage_plateforme.Entity.Enum.TripStatus;
import org.example.covoiturage_plateforme.Mapper.StopMapper;
import org.example.covoiturage_plateforme.Mapper.TripMapper;
import org.example.covoiturage_plateforme.Repository.TripRepository;
import org.example.covoiturage_plateforme.Repository.UserRepository;
import org.example.covoiturage_plateforme.Repository.VehicleRepository;
import org.example.covoiturage_plateforme.Exception.UserNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TripServiceTest {

    @Mock
    private TripRepository tripRepository;

    @Mock
    private TripMapper tripMapper;

    @Mock
    private StopMapper stopMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private TripService tripService;

    private UUID driverId;
    private UUID vehicleId;

    private Driver driver;
    private Vehicle vehicle;

    @BeforeEach
    void setUp() {
        driverId = UUID.randomUUID();
        vehicleId = UUID.randomUUID();

        driver = new Driver("John", "Doe", "john@example.com", "0600000000", "LICENSE123");
        driver.setId(driverId);

        vehicle = new Vehicle("Toyota", "Corolla", "ABC123", "White", 5, driver);
        vehicle.setId(vehicleId);
    }

    @Test
    void createTrip_success() {

        LocalDateTime departureDateTime = LocalDateTime.now().plusDays(2);

        StopCreateRequest stop1Request = new StopCreateRequest();
        stop1Request.setCity("Marrakech");
        stop1Request.setStopOrder(0);
        stop1Request.setArrivalTime(departureDateTime);
        stop1Request.setPriceFromStart(BigDecimal.ZERO);

        StopCreateRequest stop2Request = new StopCreateRequest();
        stop2Request.setCity("Casablanca");
        stop2Request.setStopOrder(1);
        stop2Request.setArrivalTime(departureDateTime.plusHours(3));
        stop2Request.setPriceFromStart(new BigDecimal("150"));

        TripCreateRequest request = new TripCreateRequest();
        request.setDepartureCity("Marrakech");
        request.setArrivalCity("Casablanca");
        request.setDepartureDateTime(departureDateTime);
        request.setAvailableSeats(4);
        request.setVehicleId(vehicleId);
        request.setStopsList(List.of(stop1Request, stop2Request));

        Stop stop1 = new Stop("Marrakech", 0, departureDateTime, BigDecimal.ZERO, null
        );

        Stop stop2 = new Stop("Casablanca", 1, departureDateTime.plusHours(3), new BigDecimal("150"), null
        );

        TripResponse expectedResponse = mock(TripResponse.class);

        when(userRepository.findByIdAndActiveTrue(driverId))
                .thenReturn(Optional.of(driver));

        when(vehicleRepository.findById(vehicleId))
                .thenReturn(Optional.of(vehicle));

        when(tripRepository.existsByVehicle_IdAndStatus(
                vehicleId,
                TripStatus.PUBLISHED
        )).thenReturn(false);

        when(stopMapper.toEntity(stop1Request)).thenReturn(stop1);
        when(stopMapper.toEntity(stop2Request)).thenReturn(stop2);

        when(tripRepository.save(any(Trip.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(tripMapper.toResponse(any(Trip.class)))
                .thenReturn(expectedResponse);

        TripResponse result = tripService.createTrip(request, driverId);

        assertThat(result).isSameAs(expectedResponse);

        ArgumentCaptor<Trip> tripCaptor =
                ArgumentCaptor.forClass(Trip.class);

        verify(tripRepository).save(tripCaptor.capture());

        Trip savedTrip = tripCaptor.getValue();

        assertThat(savedTrip.getDepartureCity()).isEqualTo("Marrakech");
        assertThat(savedTrip.getArrivalCity()).isEqualTo("Casablanca");
        assertThat(savedTrip.getAvailableSeats()).isEqualTo(4);
        assertThat(savedTrip.getStatus()).isEqualTo(TripStatus.PUBLISHED);

        assertThat(savedTrip.getDriver()).isSameAs(driver);
        assertThat(savedTrip.getVehicle()).isSameAs(vehicle);

        assertThat(savedTrip.getStops()).hasSize(2);
        assertThat(savedTrip.getStops()).containsExactly(stop1, stop2);

        assertThat(stop1.getTrip()).isSameAs(savedTrip);
        assertThat(stop2.getTrip()).isSameAs(savedTrip);

        assertThat(driver.getTrips()).contains(savedTrip);
        assertThat(vehicle.getTrips()).contains(savedTrip);

        verify(stopMapper, times(2)).toEntity(any(StopCreateRequest.class));
        verify(tripMapper).toResponse(savedTrip);
    }
}
