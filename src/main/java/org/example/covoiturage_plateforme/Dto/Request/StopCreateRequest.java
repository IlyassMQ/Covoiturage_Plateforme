package org.example.covoiturage_plateforme.Dto.Request;


import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class StopCreateRequest {


    @NotBlank
    private String city;

    @NotNull
    private Integer stopOrder;

    @NotNull
    @Future
    private LocalDateTime arrivalTime;

    @NotNull
    @PositiveOrZero
    private BigDecimal priceFromStart;
}
