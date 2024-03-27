package com.cuma.yildiz.VetSystem.dto.request.customer;

import com.cuma.yildiz.VetSystem.dto.response.entitiesResponse.AnimalResponse;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerSaveRequest {

    @NotBlank(message = "Müşteri Adı boş olamaz!")
    private String name;

    @NotBlank(message = "Telefon Numarasi boş olamaz!")
    private String phone;

    @Email
    @NotBlank(message = "E-mail adresi boş olamaz!")
    private String mail;

    private String address;

    private String city;

    //private List<AnimalResponse> animals; // Müşteriye ait hayvan bilgileri

}
