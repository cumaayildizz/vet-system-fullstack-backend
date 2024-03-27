package com.cuma.yildiz.VetSystem.business.abstracts;

import com.cuma.yildiz.VetSystem.entities.Animal;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface IAnimalService {

    Animal saveAnimal(Animal animal);

    Animal getAnimalById(Long id);

    List<Animal> getAllAnimals();

    Animal updateAnimal(Animal animal);

    void deleteAnimal(Long id);

    List<Animal> getAnimalsByName(String name);

    List<Animal> getAnimalsByCustomer(Long customerId);

    List<Animal> getAnimalsByGender(String gender);

    List<Animal> getAnimalsByDateOfBirthBetween(LocalDate startDate, LocalDate endDate);

    List<Animal> getAnimalsByCustomerName(String customerName);

    List<Animal> getAnimalsILIKEByName(String name);

    Page<Animal> cursor(int page, int pageSize);

    // Hayvan adı, türü ve cinsine göre arama
    List<Animal> findAnimalsByNameAndSpeciesAndBreed(String name, String species, String breed);

    List<Animal> findByNameAndCustomerId(String name,Long customerId);
}
