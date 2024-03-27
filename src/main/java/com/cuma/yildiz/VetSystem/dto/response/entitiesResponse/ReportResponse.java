package com.cuma.yildiz.VetSystem.dto.response.entitiesResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class ReportResponse {

    private Long id;
    private String title;
    private String diagnosis;
    private double price;
    private Long appointmentId;
    private List<VaccineResponse> vaccines;
}