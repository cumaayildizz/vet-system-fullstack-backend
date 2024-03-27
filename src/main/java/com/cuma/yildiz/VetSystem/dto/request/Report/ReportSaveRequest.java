package com.cuma.yildiz.VetSystem.dto.request.Report;
import com.cuma.yildiz.VetSystem.entities.Appointment;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportSaveRequest {
    private String title;
    private String diagnosis;
    private double price;
    private Long appointmentId;
}
