package com.cuma.yildiz.VetSystem.dao;

import com.cuma.yildiz.VetSystem.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepo extends JpaRepository<Doctor, Long> {

    // Doktor adına göre arama yapar
    List<Doctor> findByName(String name);
    List<Doctor> findByMail(String mail);
    //boolean existByName(String name);
    //boolean existByMail(String mail);

}