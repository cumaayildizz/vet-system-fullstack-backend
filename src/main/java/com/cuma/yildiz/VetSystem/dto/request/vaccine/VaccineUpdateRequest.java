package com.cuma.yildiz.VetSystem.dto.request.vaccine;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VaccineUpdateRequest {

    @Positive(message = "ID değeri pozitif sayı olmak zorunda")
    private Long id;

    @NotBlank(message = "Aşı Adı boş olamaz!")
    private String name;

    @NotBlank(message = "Aşı Kodu boş olamaz!")
    private String code;

    @NotNull(message = "Koruma Başlangıç Tarihi boş olamaz!")
    private LocalDate protectionStartDate;

    @NotNull(message = "Koruma Bitiş Tarihi boş olamaz!")
    private LocalDate protectionFinishDate;

    private Long animalId;

    private Long reportId;
}