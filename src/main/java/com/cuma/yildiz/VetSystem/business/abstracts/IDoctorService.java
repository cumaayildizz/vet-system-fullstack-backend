package com.cuma.yildiz.VetSystem.business.abstracts;

import com.cuma.yildiz.VetSystem.entities.Doctor;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IDoctorService {

    // Yeni bir doktoru kaydeder
    Doctor saveDoctor(Doctor doctor);

    // ID'ye göre bir doktoru getirir
    Doctor getDoctorById(Long id);

    // Tüm doktorları getirir
    List<Doctor> getAllDoctors();

    // Bir doktoru günceller
    Doctor updateDoctor(Doctor doctor);

    // Bir doktoru siler
    void deleteDoctor(Long id);

    // Doktor adına göre doktorları getirir
    List<Doctor> getDoctorsByName(String name);
    List<Doctor> getDoctorsByMail(String mail);

    // Sayfa sayfa tüm doktorları getirir
    Page<Doctor> cursor(int page, int pageSize);
}