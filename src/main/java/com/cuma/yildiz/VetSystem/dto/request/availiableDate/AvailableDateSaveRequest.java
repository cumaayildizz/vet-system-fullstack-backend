package com.cuma.yildiz.VetSystem.dto.request.availiableDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailableDateSaveRequest {

    @NotNull(message = "Müsait Tarih boş olamaz!")
    private LocalDate availableDate;

    @NotNull(message = "Doktor ID boş olamaz!")
    private Long doctorId;

}
