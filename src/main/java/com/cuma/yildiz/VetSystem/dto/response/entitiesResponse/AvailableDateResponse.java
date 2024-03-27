package com.cuma.yildiz.VetSystem.dto.response.entitiesResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailableDateResponse {
    private Long id;
    private LocalDate availableDate;
    private Long doctorId; // Doktora ait müsait gün
    //private List<AppointmentResponse> appointments; // Müsait güne ait randevular

}
