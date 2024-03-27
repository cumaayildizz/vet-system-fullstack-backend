package com.cuma.yildiz.VetSystem.dto.request.doctor;

import com.cuma.yildiz.VetSystem.dto.response.entitiesResponse.AppointmentResponse;
import com.cuma.yildiz.VetSystem.dto.response.entitiesResponse.AvailableDateResponse;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorUpdateRequest {

    @Positive(message = "ID değeri pozitif sayı olmak zorunda")
    private Long id;

    @NotBlank(message = "Doktor Adı boş olamaz!")
    private String name;

    @NotBlank(message = "Telefon Numarasi boş olamaz!")
    private String phone;

    @Email
    @NotBlank(message = "E-mail adresi boş olamaz!")
    private String mail;

    private String address;

    private String city;

    //private List<AvailableDateResponse> availableDates; // Doktora ait müsait günler
    //private List<AppointmentResponse> appointments; // Doktora ait randevular
}