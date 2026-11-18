package org.example.covoiturage_plateforme.Service;


import lombok.RequiredArgsConstructor;
import org.example.covoiturage_plateforme.Dto.Request.VehicleCreateRequest;
import org.example.covoiturage_plateforme.Dto.Request.VehicleUpdateRequest;
import org.example.covoiturage_plateforme.Dto.Response.VehicleResponse;
import org.example.covoiturage_plateforme.Entity.Driver;
import org.example.covoiturage_plateforme.Entity.User;
import org.example.covoiturage_plateforme.Entity.Vehicle;
import org.example.covoiturage_plateforme.Exception.UserNotFoundException;
import org.example.covoiturage_plateforme.Exception.VehicleAlreadyExistException;
import org.example.covoiturage_plateforme.Exception.VehicleNotFoundException;
import org.example.covoiturage_plateforme.Mapper.VehicleMapper;
import org.example.covoiturage_plateforme.Repository.UserRepository;
import org.example.covoiturage_plateforme.Repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleService {

        private final VehicleRepository vehicleRepository;
        private final UserRepository userRepository;
        private final VehicleMapper vehicleMapper;


        @Transactional
        public VehicleResponse createVehicle(VehicleCreateRequest request, UUID driverId){
                    Driver driver = getDriver(driverId);

                    if (vehicleRepository.existsByLicensePlate(request.getLicensePlate())){
                        throw new VehicleAlreadyExistException();
                    }


            Vehicle vehicle = new Vehicle(request.getBrand(), request.getModel(), request.getLicensePlate(), request.getColor(), request.getCapacity(), driver);
                 driver.getVehicles().add(vehicle);
                Vehicle savedVehicle =  vehicleRepository.save(vehicle);

                    return vehicleMapper.toResponse(savedVehicle);

        }



        public VehicleResponse getVehicleById(UUID vehicleId){
                Vehicle vehicle = vehicleRepository.findById(vehicleId)
                        .orElseThrow(VehicleNotFoundException::new);

                return vehicleMapper.toResponse(vehicle);

        }

    public VehicleResponse getVehicleByLicensePlate(String licencePlate){
        Vehicle vehicle = vehicleRepository.findByLicensePlate(licencePlate)
                .orElseThrow(VehicleNotFoundException::new);


        return vehicleMapper.toResponse(vehicle);

    }

    public List<VehicleResponse> getAllVehicles(){
            List<Vehicle> vehicleList = vehicleRepository.findAll();

            List<VehicleResponse> vehicleResponseList = vehicleList.stream().map(vehicleMapper::toResponse).toList();
            return vehicleResponseList;
    }

    public List<VehicleResponse> getAllDriverVehicle(UUID driverId){

            Driver driver = getDriver(driverId);

            List<Vehicle> vehicleList = vehicleRepository.getAllByDriver_Id(driver.getId());

         List<VehicleResponse> vehicleResponseList = vehicleList.stream().map(vehicleMapper::toResponse).toList();
          return vehicleResponseList;

    }


    @Transactional
    public VehicleResponse updateVehicle(UUID vehicleId , UUID driverId, VehicleUpdateRequest request){
            Driver driver = getDriver(driverId);

            Vehicle vehicle = vehicleRepository.findById(vehicleId)
                    .orElseThrow(VehicleNotFoundException::new);
            
        if (!driver.getId().equals(vehicle.getDriver().getId())){
            throw new IllegalArgumentException("You are not authorized to update this vehicle");
        }

            vehicle.setBrand(request.getBrand());
            vehicle.setCapacity(request.getCapacity());
            vehicle.setColor(request.getColor());
            vehicle.setModel(request.getModel());

            if (!(vehicle.getLicensePlate().equals(request.getLicensePlate()))){
                if (vehicleRepository.existsByLicensePlate(request.getLicensePlate())) {
                    throw new VehicleAlreadyExistException();
                }
                vehicle.setLicensePlate(request.getLicensePlate());
            }

            Vehicle updatedVehicle = vehicleRepository.save(vehicle);

           return vehicleMapper.toResponse(updatedVehicle);

    }


        private Driver getDriver(UUID driverId){

            User user = userRepository.findByIdAndActiveTrue(driverId)
                    .orElseThrow(UserNotFoundException::new);

            if (!(user instanceof Driver driver)){
                throw new IllegalArgumentException("The user is not Driver");

            }
            return driver;
        }
}
