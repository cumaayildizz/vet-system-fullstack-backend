package com.cuma.yildiz.VetSystem.dao;

import com.cuma.yildiz.VetSystem.entities.Animal;
import com.cuma.yildiz.VetSystem.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AnimalRepo extends JpaRepository<Animal, Long> {

    // Hayvan adına göre arama
    List<Animal> findByName(String name);

    // Hayvan adı, türü ve cinsine göre arama
    List<Animal> findByNameAndSpeciesAndBreed(String name, String species, String breed);

    // Hayvan sahibine göre arama
    List<Animal> findByCustomer(Customer customer);
    List<Animal> findByCustomerId(Long customerId);

    // Hayvan sahibinin adına göre arama
    @Query("SELECT a FROM Animal a WHERE a.customer.name = :customerName")
    List<Animal> findByCustomerName(@Param("customerName") String customerName);

    // Hayvan isimlerine göre büyük/küçük harf duyarlılığı olmadan arama
    @Query("SELECT a FROM Animal a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Animal> findByAnimalsILIKEName(@Param("name") String name);

    // Hayvan cinsiyetine göre arama
    List<Animal> findByGender(String gender);

    // Tarih aralığına göre arama
    @Query("SELECT a FROM Animal a WHERE a.dateOfBirth BETWEEN :startDate AND :endDate")
    List<Animal> findByDateOfBirthBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    List<Animal> findByNameAndCustomerId(String name, Long customerId);
}