package com.cuma.yildiz.VetSystem.dto.request.appointment;

import com.cuma.yildiz.VetSystem.entities.Doctor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentSaveRequest {

    @NotNull(message = "Randevu Tarihi ve Saati boş olamaz!")
    private LocalDateTime appointmentDate;

    @NotNull(message = "Hayvan ID boş olamaz!")
    private Long animalId; // Randevuya ait hayvan

    @NotNull(message = "Doktor ID boş olamaz!")
    private Long doctorId; // Randevuya ait doktor

//    @NotBlank(message = "Rapor boş kalamaz!")
//    private Long reportId;
}
