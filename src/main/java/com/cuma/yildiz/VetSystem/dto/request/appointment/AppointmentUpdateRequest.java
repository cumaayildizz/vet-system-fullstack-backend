package com.cuma.yildiz.VetSystem.dto.request.appointment;


import com.cuma.yildiz.VetSystem.entities.Animal;
import com.cuma.yildiz.VetSystem.entities.Doctor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentUpdateRequest {

    @Positive(message = "ID değeri pozitif sayı olmak zorunda")
    private Long id;

    @NotNull(message = "Randevu Tarihi ve Saati boş olamaz!")
    private LocalDateTime appointmentDate;

    @NotNull(message = "Hayvan ID boş olamaz!")
    private Long animalId;

    @NotNull(message = "Doktor ID boş olamaz!")
    private Long doctorId;

//    @NotBlank(message = "Rapor boş kalamaz!")
//    private Long reportId;
}