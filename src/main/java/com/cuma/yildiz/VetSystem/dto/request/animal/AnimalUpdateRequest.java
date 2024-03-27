package com.cuma.yildiz.VetSystem.dto.request.animal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimalUpdateRequest {

    @Positive(message = "ID değeri pozitif sayı olmak zorunda")
    private Long id;

    @NotBlank(message = "Hayvan Adı boş olamaz!")
    private String name;

    @NotBlank(message = "Hayvan Türü boş olamaz!")
    private String species;

    private String breed;

    @NotBlank(message = "Cinsiyet boş olamaz!")
    private String gender;

    private String colour;

    @NotNull(message = "Doğum Tarihi boş olamaz!")
    private LocalDate dateOfBirth;

    private Long customerId;

    //private List<VaccineResponse> vaccines; // Hayvana ait aşı bilgileri

    // Diğer gerekli alanlar
}