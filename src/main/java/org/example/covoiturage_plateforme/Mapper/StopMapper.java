package org.example.covoiturage_plateforme.Mapper;

import org.example.covoiturage_plateforme.Dto.Request.StopCreateRequest;
import org.example.covoiturage_plateforme.Dto.Response.StopResponse;
import org.example.covoiturage_plateforme.Entity.Stop;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StopMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "trip", ignore = true)
    Stop toEntity(StopCreateRequest request);

    StopResponse toResponse(Stop stop);
}