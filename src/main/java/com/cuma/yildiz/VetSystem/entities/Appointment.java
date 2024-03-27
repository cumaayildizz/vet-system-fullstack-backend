package com.cuma.yildiz.VetSystem.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private Long id;

    @Column(name = "appointment_date" , nullable = false)
    private LocalDateTime appointmentDate;

    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "animal_id" , referencedColumnName = "animal_id")
    private Animal animal;

    @ManyToOne(fetch = FetchType.LAZY , cascade = CascadeType.MERGE)
    @JoinColumn(name = "doctor_id" )
    private Doctor doctor;


    @OneToOne (mappedBy = "appointment" , cascade = CascadeType.REMOVE)
    private Report report;

}