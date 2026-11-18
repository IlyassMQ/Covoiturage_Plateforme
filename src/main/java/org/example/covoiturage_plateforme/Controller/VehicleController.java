package org.example.covoiturage_plateforme.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.covoiturage_plateforme.Dto.Request.VehicleCreateRequest;
import org.example.covoiturage_plateforme.Dto.Request.VehicleUpdateRequest;
import org.example.covoiturage_plateforme.Dto.Response.VehicleResponse;
import org.example.covoiturage_plateforme.Service.VehicleService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/drivers/vehicle")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;


    @PostMapping("/create/{driverId}")
    @ResponseStatus(HttpStatus.CREATED)
    public VehicleResponse createVehicle(@Valid @RequestBody VehicleCreateRequest request, @PathVariable("driverId")UUID driverId){

        return vehicleService.createVehicle(request,driverId);

    }

    @GetMapping("/{vehicleId}")
    @ResponseStatus(HttpStatus.OK)
    public VehicleResponse getVehicleById(@PathVariable("vehicleId") UUID vehicleId){
        return vehicleService.getVehicleById(vehicleId);
    }

    @GetMapping("/vehiclePlate")
    @ResponseStatus(HttpStatus.OK)
    public VehicleResponse getByPlate(@RequestParam("vehiclePlate") String vehiclePlate){
        return vehicleService.getVehicleByLicensePlate(vehiclePlate);
    }


    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<VehicleResponse> getAllVehicle(){
        return vehicleService.getAllVehicles();
    }

    @GetMapping("/driver/{driverId}")
    @ResponseStatus(HttpStatus.OK)
    public List<VehicleResponse> getVehiclesByDriver(@PathVariable("driverId") UUID driverId){
        return vehicleService.getAllDriverVehicle(driverId);
    }


    @PutMapping("/{driverId}/{vehicleId}")
    @ResponseStatus(HttpStatus.OK)
    public VehicleResponse updateVehicle(@PathVariable("driverId") UUID driverId, @PathVariable("vehicleId") UUID vehicleId,@Valid @RequestBody VehicleUpdateRequest request){
        return vehicleService.updateVehicle(vehicleId,driverId,request);
    }


}
