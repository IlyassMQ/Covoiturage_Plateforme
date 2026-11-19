package org.example.covoiturage_plateforme.Mapper;


import org.example.covoiturage_plateforme.Dto.Request.VehicleCreateRequest;
import org.example.covoiturage_plateforme.Dto.Request.VehicleUpdateRequest;
import org.example.covoiturage_plateforme.Dto.Response.VehicleResponse;
import org.example.covoiturage_plateforme.Entity.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    @Mapping(source = "driver.id",target = "driverId")
    VehicleResponse toResponse(Vehicle vehicle);

    Vehicle toEntity(VehicleCreateRequest request);

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "driver",ignore = true)
    @Mapping(target = "trips",ignore = true)
    void updateVehicle(VehicleUpdateRequest request, @MappingTarget Vehicle vehicle);

}
