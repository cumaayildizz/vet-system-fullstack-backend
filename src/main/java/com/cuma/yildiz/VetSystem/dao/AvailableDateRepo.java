package com.cuma.yildiz.VetSystem.dao;

import com.cuma.yildiz.VetSystem.entities.AvailableDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AvailableDateRepo extends JpaRepository<AvailableDate, Long> {
    //Doktora gore bos zamanlari bul
    List<AvailableDate> findByDoctorId(Long doctorId);

    Optional<AvailableDate> findByDoctorIdAndAvailableDate(Long doctorId, LocalDate date);


    //Bir doktorun belirli zaman araliklarinda bos zamani var mi?
    List<AvailableDate> findByDoctorIdAndAvailableDateBetween(Long doctorId, LocalDate startDate, LocalDate endDate);

}
