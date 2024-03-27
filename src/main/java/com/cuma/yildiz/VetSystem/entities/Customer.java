package com.cuma.yildiz.VetSystem.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long id;

    @Column(name = "customer_name" , nullable = false)
    private String name;

    @Column(name = "customer_phone" ,unique = true , nullable = false)
    private String phone;

    @Email
    @Column(name = "customer_mail" , unique = true , nullable = false)
    private String mail;

    @Column(name = "customer_address")
    private String address;

    @Column(name = "customer_city")
    private String city;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.REMOVE)
    private List<Animal> animals;

}