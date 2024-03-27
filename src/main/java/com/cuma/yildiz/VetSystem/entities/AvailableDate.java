package com.cuma.yildiz.VetSystem.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AvailableDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "available_date_id")
    private Long id;

    @Column(name = "available_date" , nullable = false)
    private LocalDate availableDate;

    @ManyToOne(fetch = FetchType.EAGER , cascade = CascadeType.MERGE)
    @JoinColumn(name = "doctor_id" , referencedColumnName = "doctor_id")
    private Doctor doctor;

}