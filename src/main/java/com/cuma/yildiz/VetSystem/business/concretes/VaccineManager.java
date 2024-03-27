package com.cuma.yildiz.VetSystem.business.concretes;

import com.cuma.yildiz.VetSystem.business.abstracts.IVaccineService;
import com.cuma.yildiz.VetSystem.core.config.exceptions.NotFoundException;
import com.cuma.yildiz.VetSystem.core.util.Messages;
import com.cuma.yildiz.VetSystem.dao.VaccineRepo;
import com.cuma.yildiz.VetSystem.entities.Animal;
import com.cuma.yildiz.VetSystem.entities.Vaccine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class VaccineManager implements IVaccineService {

    private final VaccineRepo vaccineRepo;

    @Autowired
    public VaccineManager(VaccineRepo vaccineRepo) {
        this.vaccineRepo = vaccineRepo;
    }


    @Override
    public Vaccine saveVaccine(Vaccine vaccine) {
        Objects.requireNonNull(vaccine, Messages.NOT_FOUND);
        Objects.requireNonNull(vaccine.getProtectionStartDate(), Messages.NULL_DATE);
        Objects.requireNonNull(vaccine.getProtectionFinishDate(), Messages.NULL_DATE);
        LocalDate today = LocalDate.now();

        // Aynı ad, kod ve hayvan ID'si ile başka bir aşı kaydı var mı kontrol et
        Optional<Vaccine> existingVaccine = vaccineRepo.findByAnimalIdAndName(
                vaccine.getAnimal().getId(), vaccine.getName());

        // Eğer aşı koruyuculuk tarihi bugün veya gelecekte bitiyorsa ve var olan bir aşı kaydı varsa hata fırlat
        if (existingVaccine.isPresent() && (existingVaccine.get().getProtectionFinishDate().isAfter(today) || existingVaccine.get().getProtectionFinishDate().isEqual(today))) {
            throw new IllegalArgumentException("Aynı ad ve hayvan ID'sine sahip bir aşı kaydı zaten mevcut ve koruyuculuk süresi devam ediyor ya da bugün bitiyor!");
        }

        if (vaccine.getProtectionStartDate().isAfter(vaccine.getProtectionFinishDate())) {
            throw new IllegalArgumentException(Messages.START_DATE_AFTER_FINISH_DATE);
        }

        return vaccineRepo.save(vaccine);
    }

    @Override
    public Vaccine updateVaccine(Vaccine vaccine) {
        Objects.requireNonNull(vaccine, Messages.NOT_FOUND);
        Objects.requireNonNull(vaccine.getProtectionStartDate(), Messages.NULL_DATE);
        Objects.requireNonNull(vaccine.getProtectionFinishDate(), Messages.NULL_DATE);

        if (vaccine.getProtectionStartDate().isAfter(vaccine.getProtectionFinishDate())) {
            throw new IllegalArgumentException(Messages.START_DATE_AFTER_FINISH_DATE);
        }

        getVaccineById(vaccine.getId()); // Vaccine var mı diye kontrol ediliyor.

        return vaccineRepo.save(vaccine);
    }

    @Override
    public boolean delete(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException(Messages.NEGATIVE_ID);
        }

        getVaccineById(id); // Vaccine var mı diye kontrol ediliyor.

        vaccineRepo.deleteById(id);
        return true;
    }

    @Override
    public Vaccine getVaccineById(Long id) {
        //Objects.requireNonNull(id, Messages.NOT_FOUND);
        return vaccineRepo.findById(id).orElseThrow(() -> new NotFoundException(Messages.NOT_FOUND));
    }

    @Override
    public List<Vaccine> getVaccinesByAnimalId(Long animalId) {
        Objects.requireNonNull(animalId, Messages.NEGATIVE_ID);
        return vaccineRepo.findByAnimalId(animalId);
    }

    @Override
    public List<Vaccine> getAllVaccines() {
        return vaccineRepo.findAll();
    }

    @Override
    public List<Animal> getAnimalsWithUpcomingVaccines(LocalDate startDate, LocalDate endDate) {
        Objects.requireNonNull(startDate, Messages.NULL_DATE);
        Objects.requireNonNull(endDate, Messages.NULL_DATE);

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException(Messages.START_DATE_AFTER_FINISH_DATE);
        }

        return vaccineRepo.findByProtectionStartDateBetween(startDate, endDate)
                .stream()
                .map(Vaccine::getAnimal)
                .distinct()
                .toList();
    }

    @Override
    public List<Vaccine> getVaccinesByDateRange(LocalDate startDate, LocalDate endDate) {
        Objects.requireNonNull(startDate, Messages.NULL_DATE);
        Objects.requireNonNull(endDate, Messages.NULL_DATE);

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException(Messages.START_DATE_AFTER_FINISH_DATE);
        }

        return vaccineRepo.findByProtectionStartDateBetween(startDate, endDate);
    }

    @Override
    public List<Vaccine> findByAnimalIdAndProtectionStartDateBetween(Long animalId, LocalDate startDate, LocalDate endDate) {
        Objects.requireNonNull(animalId, Messages.NEGATIVE_ID);
        Objects.requireNonNull(startDate, Messages.NULL_DATE);
        Objects.requireNonNull(endDate, Messages.NULL_DATE);

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException(Messages.START_DATE_AFTER_FINISH_DATE);
        }

        return vaccineRepo.findByAnimalIdAndProtectionStartDateBetween(animalId, startDate, endDate);
    }

    @Override
    public Page<Vaccine> cursor(int page, int pageSize) {
        if (page < 0 || pageSize <= 0) {
            throw new IllegalArgumentException(Messages.INVALID_PAGE_OR_PAGE_SIZE);
        }

        Pageable pageable = PageRequest.of(page, pageSize);
        return vaccineRepo.findAll(pageable);
    }
}
