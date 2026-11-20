package org.example.covoiturage_plateforme.Dto.Response;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class StopResponse {
    private UUID id;
    private String city;
    private Integer stopOrder;
    private LocalDateTime arrivalTime;
    private BigDecimal priceFromStart;
}
