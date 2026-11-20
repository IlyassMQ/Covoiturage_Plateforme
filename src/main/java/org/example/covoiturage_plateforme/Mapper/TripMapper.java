
package org.example.covoiturage_plateforme.Mapper;

import org.example.covoiturage_plateforme.Dto.Request.TripCreateRequest;
import org.example.covoiturage_plateforme.Dto.Response.TripResponse;
import org.example.covoiturage_plateforme.Entity.Trip;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TripMapper {

    @Mapping(source = "driver.id", target = "driverId")
    @Mapping(source = "vehicle.id", target = "vehicleId")
    @Mapping(source = "stops", target = "stops")
    TripResponse toResponse(Trip trip);

    Trip toEntity(TripCreateRequest request);
}
