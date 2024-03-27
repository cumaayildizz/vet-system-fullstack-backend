package com.cuma.yildiz.VetSystem.dto.request.Report;

import com.cuma.yildiz.VetSystem.entities.Appointment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportUpdateRequest {
    private long id;
    private String title;
    private String diagnosis;
    private double price;
    private Long appointmentId;
}
