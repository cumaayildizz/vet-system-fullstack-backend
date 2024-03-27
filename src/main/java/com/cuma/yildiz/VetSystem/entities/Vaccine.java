package com.cuma.yildiz.VetSystem.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Vaccine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vaccine_id")
    private Long id;

    @Column(name = "vaccine_name", nullable = false)
    private String name;

    @Column(name = "vaccine_code", nullable = false, unique = true)
    private String code;

    @Column(name = "protection_start_date")
    private LocalDate protectionStartDate;

    @Column(name = "protection_finish_date")
    private LocalDate protectionFinishDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "animal_id", referencedColumnName = "animal_id")
    private Animal animal;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "report_id", referencedColumnName = "report_id")
    private Report report;

}





//package com.cuma.yildiz.VetSystem.entities;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import jakarta.persistence.Entity;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDate;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//public class Vaccine {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "vaccine_id")
//    private Long id;
//
//    @Column(name = "vaccine_name" , nullable = false)
//    private String name;
//
//    @Column(name = "vaccine_code" , nullable = false , unique = true )
//    private String code;
//
//    @Column(name = "protection_start_date")
//    private LocalDate protectionStartDate;
//
//    @Column(name = "protection_finish_date")
//    private LocalDate protectionFinishDate;
//
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "animal_id"  , referencedColumnName = "animal_id")
//    private Animal animal;
//
//
//    @ManyToOne (fetch = FetchType.EAGER)
//    @JoinColumn (name = "report_id" , referencedColumnName = "report_id")
//    private Report report;
//
//}