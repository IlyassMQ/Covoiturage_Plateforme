
package org.example.covoiturage_plateforme.Mapper;

import org.example.covoiturage_plateforme.Dto.Response.ReservationResponse;
import org.example.covoiturage_plateforme.Entity.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = StopMapper.class)
public interface ReservationMapper {

    @Mapping(source = "passenger.id", target = "passengerId")
    @Mapping(source = "trip.id", target = "tripId")
    ReservationResponse toResponse(Reservation reservation);

}
