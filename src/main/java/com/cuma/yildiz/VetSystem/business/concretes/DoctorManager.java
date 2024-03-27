package com.cuma.yildiz.VetSystem.business.concretes;

import com.cuma.yildiz.VetSystem.business.abstracts.IDoctorService;
import com.cuma.yildiz.VetSystem.business.rules.DoctorBusinessRules;
import com.cuma.yildiz.VetSystem.core.config.exceptions.NotFoundException;
import com.cuma.yildiz.VetSystem.core.util.Messages;
import com.cuma.yildiz.VetSystem.dao.DoctorRepo;
import com.cuma.yildiz.VetSystem.entities.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.util.List;

@Service
public class DoctorManager implements IDoctorService {
    private final DoctorRepo doctorRepo;
    private final DoctorBusinessRules doctorBusinessRules;

    public DoctorManager(DoctorRepo doctorRepo, DoctorBusinessRules doctorBusinessRules) {
        this.doctorRepo = doctorRepo;
        this.doctorBusinessRules = doctorBusinessRules;
    }

    @Override
    public Doctor saveDoctor(Doctor doctor) {

        //Burayi iyi incele!!!
        this.doctorBusinessRules.checkIfDoctorExists(doctor.getName() , doctor.getMail());

        if (doctor == null) {
            throw new IllegalArgumentException(Messages.NOT_FOUND);
        }

        if (doctor.getName() == null || doctor.getName().isEmpty()) {
            throw new NotFoundException(Messages.DOCTOR_NAME_NOT_FOUND);
        }

        return doctorRepo.save(doctor);
    }

    @Override
    public Doctor getDoctorById(Long id) {
        return doctorRepo.findById(id).orElseThrow(() -> new NotFoundException(Messages.NOT_FOUND));
    }

    @Override
    public List<Doctor> getAllDoctors() {
        List<Doctor> doctors = doctorRepo.findAll();

        if (doctors == null || doctors.isEmpty()) {
            throw new RuntimeException(Messages.NOT_FOUND);
        }
        return doctorRepo.findAll();
    }

    @Override
    public Doctor updateDoctor(Doctor doctor) {
        getDoctorById(doctor.getId());

        if (doctor.getId() == null) {
            throw new RuntimeException(Messages.DOCTOR_ID_NOT_FOUND);
        }

        if (doctor.getName() == null || doctor.getName().isEmpty()) {
            throw new NotFoundException(Messages.DOCTOR_NAME_NOT_FOUND);
        }

        return doctorRepo.save(doctor);
    }

    @Override
    public void deleteDoctor(Long id) {
        getDoctorById(id);

        if (id != null && id > 0) {
            doctorRepo.deleteById(id);
        } else if (id != null && id <= 0) {
            throw new IllegalArgumentException(Messages.NEGATIVE_ID);
        } else {
            throw new IllegalArgumentException(Messages.ANIMAL_ID_NOT_FOUND);
        }
    }

    @Override
    public List<Doctor> getDoctorsByName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException(Messages.NOT_FOUND);
        }

        List<Doctor> doctors = doctorRepo.findByName(name);

        if (doctors == null || doctors.isEmpty()) {
            throw new NotFoundException(Messages.NOT_FOUND);
        }
        return doctors;
    }

    @Override
    public  List<Doctor> getDoctorsByMail (String mail){
        if (mail == null || mail.isEmpty()) {
            throw new IllegalArgumentException(Messages.NOT_FOUND);
        };
        List<Doctor> doctors = doctorRepo.findByMail(mail);
        if (doctors == null || doctors.isEmpty()) {
            throw new NotFoundException(Messages.NOT_FOUND);
        }
        return doctors;
    };

    @Override
    public Page<Doctor> cursor(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return doctorRepo.findAll(pageable);
    }
}