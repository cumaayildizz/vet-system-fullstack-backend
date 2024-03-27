package com.cuma.yildiz.VetSystem.business.concretes;


import com.cuma.yildiz.VetSystem.business.abstracts.IAvailableDateService;
import com.cuma.yildiz.VetSystem.core.config.exceptions.NotFoundException;
import com.cuma.yildiz.VetSystem.core.util.Messages;
import com.cuma.yildiz.VetSystem.dao.AvailableDateRepo;

import com.cuma.yildiz.VetSystem.dao.DoctorRepo;
import com.cuma.yildiz.VetSystem.entities.AvailableDate;
import com.cuma.yildiz.VetSystem.entities.Doctor;
import com.cuma.yildiz.VetSystem.entities.Vaccine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AvailableDateManager implements IAvailableDateService {
    private final AvailableDateRepo availableDateRepo;
    private final DoctorRepo doctorRepo;


    public AvailableDateManager(AvailableDateRepo availableDateRepo, DoctorRepo doctorRepo) {
        this.availableDateRepo = availableDateRepo;
        this.doctorRepo = doctorRepo;
    }
    @Override
    public AvailableDate saveAvailableDate(AvailableDate availableDate) {
        //TODO: Null kontrollerını DTO larda YAP!!!
        // @Positive Bunu kullanirsan tekrardan Null ve empty sorgusuna gerek kalmaz

        // Aynı ad, kod ve hayvan ID'si ile başka bir aşı kaydı var mı kontrol et
        Optional<AvailableDate> existingAvailiableDate = availableDateRepo.findByDoctorIdAndAvailableDate(
                availableDate.getDoctor().getId() , availableDate.getAvailableDate());

        // Eğer aşı koruyuculuk tarihi bitmemişse ve var olan bir aşı kaydı varsa hata fırlat
        if (!existingAvailiableDate.isEmpty()
                && existingAvailiableDate.get().getAvailableDate().equals(availableDate.getAvailableDate())
                && existingAvailiableDate.get().getDoctor().getId() == availableDate.getDoctor().getId()
        ) {
            throw new IllegalArgumentException("Doktorun bu tarihte boş zamanı bulunmuyor!");
        }

        LocalDate today = LocalDate.now();
        if (availableDate.getAvailableDate().isBefore(today)){
            throw new IllegalArgumentException("Girdiğiniz tarih geçmiş zamanlı.Lütfen geçerli bir tarih giriniz!!");
        }

        return this.availableDateRepo.save(availableDate);
    }

    @Override
    public AvailableDate getAvailableDateById(Long id) {
        return this.availableDateRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(Messages.NOT_FOUND));
    }

    @Override
    public List<AvailableDate> getAllAvailableDates() {
        List<AvailableDate> availableDates = availableDateRepo.findAll();


        return this.availableDateRepo.findAll();
    }

    @Override
    public AvailableDate updateAvailableDate(AvailableDate availableDate) {
        if (availableDate == null || availableDate.getId() == null || availableDate.getId() <= 0) {
            throw new IllegalArgumentException(Messages.NOT_FOUND);
        }

        AvailableDate existingAvailableDate = this.availableDateRepo.findById(availableDate.getId())
                .orElseThrow(() -> new NotFoundException(Messages.NOT_FOUND));

        existingAvailableDate.setDoctor(availableDate.getDoctor());
        existingAvailableDate.setAvailableDate(availableDate.getAvailableDate());

        return this.availableDateRepo.save(existingAvailableDate);
    }

    @Override
    public void deleteAvailableDate(Long id) {
        this.getAvailableDateById(id);
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(Messages.NOT_FOUND);
        }
        getAvailableDateById(id);
        AvailableDate availableDate = getAvailableDateById(id);
        this.availableDateRepo.delete(availableDate);
    }

    @Override
    public List<AvailableDate> getAvailableDatesByDoctorId(Long doctorId) {
        if (doctorId == null || doctorId <= 0) {
            throw new IllegalArgumentException(Messages.DOCTOR_ID_NOT_FOUND);
        }
        return this.availableDateRepo.findByDoctorId(doctorId);
    }

    @Override
    public boolean isDoctorAvailableOnDate(Long doctorId, LocalDate date) {
        if (doctorId == null || doctorId <= 0 || date == null) {
            throw new IllegalArgumentException(Messages.NOT_FOUND);
        }
        List<AvailableDate> availableDates = this.availableDateRepo.findByDoctorId(doctorId);

        // Doktorun uygun olduğu tarihler arasında kontrol yap
        for (AvailableDate availableDate : availableDates) {
            if (availableDate.getAvailableDate().equals(date)) {
                return true; // Doktor belirtilen tarihte uygun
            }
        }
        return false; // Doktor belirtilen tarihte uygun değil
    }


    @Override
    public List<AvailableDate> getAvailableDatesByDoctorIdAndDateRange(Long doctorId, LocalDate startDate, LocalDate endDate) {

        if (doctorId == null || doctorId <= 0 || startDate == null || endDate == null || startDate.isAfter(endDate)) {
            throw new IllegalArgumentException(Messages.MISSING_REQUIRED_FIELDS);
        }

        List<AvailableDate> availableDates = this.availableDateRepo.findByDoctorIdAndAvailableDateBetween(doctorId, startDate, endDate);

        if (availableDates == null || availableDates.isEmpty()) {
            throw new NotFoundException("Doktorun girilen tarih aralığına ait boş zamani bulunamadı");
        }

        return availableDates;
    }

    @Override
    public Page<AvailableDate> cursor(int page, int pageSize) {
        if (page < 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Geçersiz sayfa veya sayfa boyutu değerleri");
        }
        Pageable pageable = PageRequest.of(page, pageSize);
        return this.availableDateRepo.findAll(pageable);
    }
}

