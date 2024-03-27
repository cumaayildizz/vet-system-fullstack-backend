package com.cuma.yildiz.VetSystem.business.concretes;

import com.cuma.yildiz.VetSystem.business.abstracts.IAnimalService;
import com.cuma.yildiz.VetSystem.core.config.exceptions.NotFoundException;
import com.cuma.yildiz.VetSystem.core.util.Messages;
import com.cuma.yildiz.VetSystem.core.util.ResultHelper;
import com.cuma.yildiz.VetSystem.dao.AnimalRepo;
import com.cuma.yildiz.VetSystem.entities.Animal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AnimalManager implements IAnimalService {

    private final AnimalRepo animalRepo;

    @Autowired
    public AnimalManager(AnimalRepo animalRepo) {
        this.animalRepo = animalRepo;
    }

    @Override
    public Animal saveAnimal(Animal animal) {
        String name = animal.getName();
        Long customerId = animal.getCustomer().getId();

        // Aynı müşteriye ait başka bir hayvanın aynı ismi var mı kontrolü
        List<Animal> existingAnimals = animalRepo.findByNameAndCustomerId(name, customerId);
        if (!existingAnimals.isEmpty()) {
            throw new RuntimeException("Aynı müşteriye ait başka bir hayvan zaten bu ismi kullanıyor.");
        }

        if (name == null || name.isEmpty()) {
            throw new NotFoundException(Messages.NOT_FOUND);
        }

        LocalDate currentDate = LocalDate.now();
        LocalDate birthDate = animal.getDateOfBirth();

        if (birthDate != null && birthDate.isAfter(currentDate)) {
            throw new RuntimeException("Doğum tarihi boş ve ya bugünden sonraki bir tarih olamaz.");
        }

        return animalRepo.save(animal);
    }

    @Override
    public Animal getAnimalById(Long id) {
        return animalRepo.findById(id).orElseThrow(() -> new NotFoundException(Messages.NOT_FOUND));
    }

    @Override
    public List<Animal> getAllAnimals() {
        List<Animal> animals = animalRepo.findAll();
        if (animals == null || animals.isEmpty()) {
            throw new RuntimeException(Messages.NOT_FOUND);
        }
        return animals;
    }

    @Override
    public Animal updateAnimal(Animal animal) {
        this.getAnimalById(animal.getId());
        if (animal.getId() == null) {
            throw new RuntimeException(animal.getId() + Messages.ANIMAL_ID_NOT_FOUND);
        }
        if (animal.getName() == null || animal.getName().isEmpty()) {
            throw new IllegalArgumentException(Messages.BAD_REQUEST);
        }
        LocalDate currentDate = LocalDate.now();
        LocalDate birthDate = animal.getDateOfBirth();

        if (birthDate != null && birthDate.isAfter(currentDate)) {
            throw new RuntimeException("Doğum tarihi bugünden sonraki bir tarih olamaz.");
        }
        return animalRepo.save(animal);
    }

    @Override
    public void deleteAnimal(Long id) {
        this.getAnimalById(id);
        if (id != null && id > 0) {
            animalRepo.deleteById(id);
        } else if (id != null && id <= 0) {
            throw new IllegalArgumentException(id + Messages.NEGATIVE_ID);
        } else {
            throw new IllegalArgumentException(id + Messages.ANIMAL_ID_NOT_FOUND);
        }
    }

    @Override
    public List<Animal> getAnimalsByName(String name) {
        if (name == null || name.isEmpty()) {
            ResultHelper.ExceptionMessage(Messages.NOT_FOUND, "404");
        }
        return animalRepo.findByName(name);
    }

    @Override
    public List<Animal> getAnimalsByCustomer(Long customerId) {
        List<Animal> animals = animalRepo.findByCustomerId(customerId);
        if(animals.isEmpty()){
            throw  new RuntimeException(Messages.NOT_FOUND);
        }
        return animals;
    }

    @Override
    public List<Animal> getAnimalsByGender(String gender) {
        if (gender == null) {
            throw new RuntimeException(Messages.BAD_REQUEST);
        }
        return animalRepo.findByGender(gender);
    }

    @Override
    public List<Animal> getAnimalsByDateOfBirthBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Başlangıç tarihi ve bitiş tarihi parametreleri boş olamaz");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Başlangıç tarihi, bitiş tarihinden önce veya aynı olmalıdır");
        }
        return animalRepo.findByDateOfBirthBetween(startDate, endDate);
    }



    @Override
    public List<Animal> getAnimalsByCustomerName(String customerName) {
        if (customerName == null || customerName.isEmpty()) {
            throw new IllegalArgumentException(customerName + Messages.CUSTOMER_NAME_NOT_FOUND);
        }
        return animalRepo.findByCustomerName(customerName);
    }

    @Override
    public List<Animal> getAnimalsILIKEByName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException(Messages.ANIMAL_NAME_NOT_FOUND);
        }
        List<Animal> animals = animalRepo.findByAnimalsILIKEName(name);
        if (animals.isEmpty()) {
            throw new IllegalArgumentException(Messages.NOT_FOUND);
        }
        return animals;
    }

    @Override
    public Page<Animal> cursor(int page, int pageSize) {
        if (page < 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Geçersiz sayfa veya sayfa boyutu değerleri");
        }
        Pageable pageable = PageRequest.of(page, pageSize);
        return this.animalRepo.findAll(pageable);
    }

    @Override
    public List<Animal> findAnimalsByNameAndSpeciesAndBreed(String name, String species, String breed) {
        if (name == null || name.isEmpty() || species == null || species.isEmpty() || breed == null || breed.isEmpty()) {
            throw new IllegalArgumentException("Ad, tür ve cins degeri boş olamaz");
        }
        List<Animal> animals = animalRepo.findByNameAndSpeciesAndBreed(name, species, breed);
        if (animals.isEmpty()) {
            throw new IllegalArgumentException(Messages.NOT_FOUND);
        }
        return animals;
    }


    @Override
    public List<Animal> findByNameAndCustomerId(String name, Long customerId) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException(Messages.ANIMAL_NAME_NOT_FOUND);
        }

        if (customerId == null || customerId <= 0) {
            throw new IllegalArgumentException(Messages.CUSTOMER_ID_NOT_FOUND);
        }

        List<Animal> animals = animalRepo.findByNameAndCustomerId(name, customerId);

        if (animals.isEmpty()) {
            throw new NotFoundException(Messages.NOT_FOUND);
        }

        return animals;
    }
}


/*package com.cuma.yildiz.VetSystem.business.concretes;

import com.cuma.yildiz.VetSystem.business.abstracts.IAnimalService;
import com.cuma.yildiz.VetSystem.core.config.exceptions.NotFoundException;
import com.cuma.yildiz.VetSystem.core.util.Messages;
import com.cuma.yildiz.VetSystem.core.util.ResultHelper;
import com.cuma.yildiz.VetSystem.dao.AnimalRepo;
import com.cuma.yildiz.VetSystem.entities.Animal;
import com.cuma.yildiz.VetSystem.entities.Appointment;
import com.cuma.yildiz.VetSystem.entities.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AnimalManager implements IAnimalService {
    private final AnimalRepo animalRepo;

    @Autowired
    public AnimalManager(AnimalRepo animalRepo) {
        this.animalRepo = animalRepo;
    }

    @Override
    public Animal saveAnimal(Animal animal) {

        if (animal.getName() == null || animal.getName().isEmpty()) {
            throw new NotFoundException(Messages.NOT_FOUND);
        }
        return animalRepo.save(animal);
    }

    @Override
    public Animal getAnimalById(Long id) {
        return animalRepo.findById(id).orElseThrow(() -> new NotFoundException(Messages.NOT_FOUND));
        //return animalRepo.findById(id).orElseThrow(() -> new AnimalNotFoundException("Hayvan bulunamadı, id: " + id));

    }

    @Override
    public List<Animal> getAllAnimals() {

        List<Animal> animals = animalRepo.findAll();

        if (animals == null || animals.isEmpty()) {
            throw new RuntimeException(Messages.NOT_FOUND);
        }

        return animalRepo.findAll();
    }


    @Override
    public Animal updateAnimal(Animal animal) {
        // ID kontrolü
        this.getAnimalById(animal.getId());
        if (animal.getId() == null) {
            throw new RuntimeException(animal.getId() + Messages.ANIMAL_ID_NOT_FOUND);
        }
        // Ad kontrolü düzenlendi
        if (animal.getName() == null || animal.getName().isEmpty()) {
            throw new IllegalArgumentException(Messages.BAD_REQUEST);
        }

        return animalRepo.save(animal);
    }

    @Override
    public void deleteAnimal(Long id) {
        this.getAnimalById(id);
        if (id != null || id > 0) {
            animalRepo.deleteById(id);
        }else if (id <= 0){
            throw new IllegalArgumentException(id + Messages.NEGATIVE_ID);
        }else {
            throw new IllegalArgumentException(id + Messages.ANIMAL_ID_NOT_FOUND);
        }

    }

    @Override
    public List<Animal> getAnimalsByName(String name) {
        if (name == null || name.isEmpty()) {
            //throw new IllegalArgumentException(name + Messages.NOT_FOUND);
            ResultHelper.ExceptionMessage(Messages.NOT_FOUND , "404");
        }
        return animalRepo.findByName(name);
    }

    @Override
    public List<Animal> getAnimalsBycustomer(Customer customer) {
        if (customer == null) {
            throw new RuntimeException(Messages.NOT_FOUND);
        }
        return animalRepo.findBycustomer(customer);
    }

    @Override
    public List<Animal> getAnimalsByGender(String gender) {
        if (gender == null) {
            throw new RuntimeException(Messages.BAD_REQUEST);
        }
        return animalRepo.findByGender(gender);
    }

    @Override
    public List<Animal> getAnimalsByDateOfBirthBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null ) {
            throw new IllegalArgumentException("Başlangıç tarihi ve bitiş tarihi parametreleri boş olamaz");
        }
        if (startDate.isAfter(endDate)) { //compareTo0
            throw new IllegalArgumentException("Başlangıç tarihi, bitiş tarihinden önce veya aynı olmalıdır");
        }
        return animalRepo.findByDateOfBirthBetween(startDate , endDate);
    }

    @Override
    public List<Animal> getAnimalsBycustomerName(String customerName) {
        if (customerName == null || customerName.isEmpty()) {
            throw new IllegalArgumentException(customerName + Messages.CUSTOMER_NAME_NOT_FOUND);
        }
        return animalRepo.findBycustomerName(customerName);
    }

    @Override
    public List<Animal> getAnimalsILIKEByName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException(Messages.ANIMAL_NAME_NOT_FOUND);
        }
        List<Animal> animals = animalRepo.findByAnimalsILIKEName(name);
        if (animals.isEmpty()) {
            throw new IllegalArgumentException(Messages.NOT_FOUND);
        }
        return animals;
    }

    @Override
    public Page<Animal> cursor(int page, int pageSize) {
        if (page < 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Geçersiz sayfa veya sayfa boyutu değerleri");
        }
        Pageable pageable = PageRequest.of(page, pageSize);
        return this.animalRepo.findAll(pageable);
    }

    @Override
    public List<Animal> findAnimalsByNameAndSpeciesAndBreed(String name, String species, String breed) {
        if (name == null || name.isEmpty() || species == null || species.isEmpty() || breed == null || breed.isEmpty()) {
            throw new IllegalArgumentException("Ad, tür ve cins degeri boş olamaz");
        }
        List<Animal> animals = animalRepo.findByNameAndSpeciesAndBreed(name, species, breed);
        if (animals.isEmpty()) {
            throw new IllegalArgumentException(Messages.NOT_FOUND);
        }
        return animals;
    }


}


*/