package com.cuma.yildiz.VetSystem.business.abstracts;

import com.cuma.yildiz.VetSystem.entities.AvailableDate;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface IAvailableDateService {
    AvailableDate saveAvailableDate(AvailableDate availableDate);

    AvailableDate getAvailableDateById(Long id);

    List<AvailableDate> getAllAvailableDates();

    AvailableDate updateAvailableDate(AvailableDate availableDate);

    void deleteAvailableDate(Long id);

    List<AvailableDate> getAvailableDatesByDoctorId(Long doctorId);

    //Doktur girilen tarihler arasinda musait mi?
    boolean isDoctorAvailableOnDate(Long doctorId, LocalDate date);

    //Bir doktorun belirli zaman araliklarinda bos zamani var mi?
    List<AvailableDate> getAvailableDatesByDoctorIdAndDateRange(Long doctorId, LocalDate startDate, LocalDate endDate);

    Page<AvailableDate> cursor(int page , int pageSize);

}