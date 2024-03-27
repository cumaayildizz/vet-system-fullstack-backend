package com.cuma.yildiz.VetSystem.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id")
    private Long id;

    @Column(name = "doctor_name" , nullable = false)
    private String name;
    @Column(name = "doctor_phone" )
    private String phone;
    @Column(name = "doctor_mail" , unique = true)
    private String mail;
    @Column(name = "doctor_address")
    private String address;
    @Column(name = "doctor_city")
    private String city;

    //@JsonIgnore
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.REMOVE , fetch = FetchType.LAZY)
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.REMOVE)
    private List<AvailableDate> availableDates;

}