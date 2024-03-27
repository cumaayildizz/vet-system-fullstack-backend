package com.cuma.yildiz.VetSystem.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "animal_id")
    private Long id;

    @Column(name = "animal_name", nullable = false)
    private String name;
    @Column(name = "animal_species")
    private String species;
    @Column(name = "animal_breed")
    private String breed;
    @Column(name = "animal_gender")
    private String gender;
    @Column(name = "animal_color")
    private String colour;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @ManyToOne(fetch = FetchType.EAGER )
    @JoinColumn(name = "customer_id"  , referencedColumnName = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.REMOVE , fetch = FetchType.LAZY)
    private List<Vaccine> vaccines;

    //@JsonIgnore
    @OneToMany(mappedBy = "animal", cascade = CascadeType.REMOVE , fetch = FetchType.LAZY)
    private List<Appointment> appointments;
}