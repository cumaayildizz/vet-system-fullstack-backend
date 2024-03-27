package com.cuma.yildiz.VetSystem.dto.request.vaccine;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VaccineSaveRequest {

    @NotNull(message = "Aşı Adı boş olamaz!")
    private String name;

    @NotNull(message = "Aşı Kodu boş olamaz!")
    private String code;

    @NotNull(message = "Koruma Başlangıç Tarihi boş olamaz!")
    private LocalDate protectionStartDate;

    @NotNull(message = "Koruma Bitiş Tarihi boş olamaz!")
    private LocalDate protectionFinishDate;

    private Long animalId;

    private  Long reportId;
}
