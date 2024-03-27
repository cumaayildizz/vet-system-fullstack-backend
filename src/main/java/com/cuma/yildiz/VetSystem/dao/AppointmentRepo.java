package com.cuma.yildiz.VetSystem.dao;

import com.cuma.yildiz.VetSystem.entities.Appointment;
import com.cuma.yildiz.VetSystem.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepo extends JpaRepository<Appointment, Long> {

    // Hayvan ID'sine göre randevuları bulma
    List<Appointment> findByAnimalId(Long animalId);

    // Doktor ID'sine göre randevuları bulma
    List<Appointment> findByDoctorId(Long doctorId);

    // Doktor ve tarih bilgisine göre randevuları bulma

    Optional<Appointment> findByDoctorIdAndAppointmentDate(Long doctorId , LocalDateTime appointmentDate);


    @Query("SELECT a FROM Appointment a WHERE a.appointmentDate BETWEEN :startDate AND :endDate")
    List<Appointment> findByAppointmentDateBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT a FROM Appointment a WHERE a.appointmentDate BETWEEN :startDate AND :endDate AND a.animal.id = :animalId")
    List<Appointment> findByAppointmentDateAndAnimalIdBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("animalId") Long animalId
    );

    @Query("SELECT a FROM Appointment a WHERE a.appointmentDate BETWEEN :startDate AND :endDate " +
            "AND a.animal.id = :animalId AND a.doctor.id = :doctorId ")
    List<Appointment> findByAppointmentDateAndAnimalIdAndDoctorIdBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("animalId") Long animalId,
            @Param("doctorId") Long doctorId
    );

    @Query("SELECT a FROM Appointment a WHERE a.appointmentDate BETWEEN :startDate AND :endDate " +
            "AND a.doctor.id = :doctorId ")
    List<Appointment> findByAppointmentDateAndDoctorIdBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("doctorId") Long doctorId
    );

}


