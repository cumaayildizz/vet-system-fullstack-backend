package com.cuma.yildiz.VetSystem.dao;

import com.cuma.yildiz.VetSystem.entities.Animal;
import com.cuma.yildiz.VetSystem.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long> {

    // Müşteri adına göre müşteri arama
    List<Customer> findByName(String name);

    // Belirli bir hayvanın sahibini bulma
    List<Customer> findByAnimals_Id(Long animalId);



}

// Müşteriye ait tüm hayvanları bulma (Query ile)
//@Query("SELECT c.animals FROM Customer c WHERE c.id = :customerId")
//List<Animal> findAnimalsByCustomerId(@Param("customerId") Long customerId);


