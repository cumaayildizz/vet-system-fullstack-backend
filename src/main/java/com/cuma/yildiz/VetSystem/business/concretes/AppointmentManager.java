package com.cuma.yildiz.VetSystem.business.concretes;

import com.cuma.yildiz.VetSystem.business.abstracts.IAppointmentService;
import com.cuma.yildiz.VetSystem.core.config.exceptions.NotFoundException;
import com.cuma.yildiz.VetSystem.core.util.Messages;
import com.cuma.yildiz.VetSystem.dao.AppointmentRepo;
import com.cuma.yildiz.VetSystem.dao.AvailableDateRepo;
import com.cuma.yildiz.VetSystem.entities.Appointment;
import com.cuma.yildiz.VetSystem.entities.AvailableDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentManager implements IAppointmentService {
    private final AppointmentRepo appointmentRepo;
    private final AvailableDateRepo availableDateRepo;


    public AppointmentManager(AppointmentRepo appointmentRepo, AvailableDateRepo availableDateRepo) {
        this.appointmentRepo = appointmentRepo;
        this.availableDateRepo = availableDateRepo;
    }


     //Yeni bir randevu kaydı oluşturma.
    @Override
    public Appointment saveAppointment(Appointment appointment) {
        // Randevu tarihi kontrolü
        LocalDateTime appointmentDate = appointment.getAppointmentDate();
        if ( appointmentDate.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Geçmiş tarihli  bir randevu tarihi seçilemez.");
        }

        // Aynı  doktor ID'si ve ayni tarihli  başka bir randevu var mı kontrol et
        Optional<Appointment> existingAppointment = appointmentRepo.findByDoctorIdAndAppointmentDate(
                appointment.getDoctor().getId() , appointment.getAppointmentDate());

        if (!existingAppointment.isEmpty()) {
            throw new IllegalArgumentException("Bu dokturun bu tarihte baska bir randevusu mevcut!!");

        }
        LocalDate date = appointmentDate.toLocalDate();
        Optional<AvailableDate> existingAvailiableDate = availableDateRepo
                .findByDoctorIdAndAvailableDate(appointment.getDoctor().getId() , date);
        if (existingAvailiableDate.isEmpty()){
            throw new RuntimeException("Doktorun bu tarihte müsait günleri yoktur.");
        }

        return appointmentRepo.save(appointment);
    }


    @Override
    public Appointment getAppointmentById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(Messages.APPOINTMENT_NOT_FOUND);
        }
        return appointmentRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(Messages.NOT_FOUND));
    }


    @Override
    public List<Appointment> getAllAppointments() {
        try {
            return appointmentRepo.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Randevular alınırken hata oluştu", e);
        }
    }


    @Override
    public Appointment updateAppointment(Appointment appointment) {
        this.getAppointmentById(appointment.getId());
        if (appointment == null || appointment.getId() == null || appointment.getId() <= 0) {
            throw new IllegalArgumentException(Messages.APPOINTMENT_NOT_FOUND);
        }
        getAppointmentById(appointment.getId());
        return appointmentRepo.save(appointment);
    }


    @Override
    public void deleteAppointment(Long id) {
        this.getAppointmentById(id);
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(Messages.APPOINTMENT_NOT_FOUND);
        }
        appointmentRepo.deleteById(id);
    }

    //Doktor ID gore randevukari getir
    @Override
    public List<Appointment> findAppointmentsByDoctor(Long doctorId) {
        if (doctorId == null || doctorId <= 0) {
            throw new IllegalArgumentException(Messages.DOCTOR_ID_NOT_FOUND);
        }
        return appointmentRepo.findByDoctorId(doctorId);
    }

    //Animal ID gore randevukari getir
    @Override
    public List<Appointment> findAppointmentsByAnimal(Long animalId) {
        if (animalId == null || animalId <= 0) {
            throw new IllegalArgumentException(Messages.ANIMAL_ID_NOT_FOUND);
        }
        return appointmentRepo.findByAnimalId(animalId);
    }

    @Override
    public List<Appointment> findByAppointmentDateAndAnimalIdAndDoctorIdBetween(LocalDateTime startDate, LocalDateTime endDate, Long animalId, Long doctorId) {
        if (startDate == null || endDate == null || animalId == null || animalId <= 0 || doctorId == null || doctorId <= 0) {
            throw new IllegalArgumentException("Geçersiz tarih veya hayvan ID");
        }
        return appointmentRepo.findByAppointmentDateAndAnimalIdAndDoctorIdBetween(startDate, endDate, animalId ,doctorId);
    }

    @Override
    public List<Appointment> findByAppointmentDateAndDoctorIdBetween(LocalDateTime startDate, LocalDateTime endDate, Long doctorId) {
        if (startDate == null || endDate == null  || doctorId == null || doctorId <= 0) {
            throw new IllegalArgumentException("Geçersiz tarih veya hayvan ID");
        }
        return appointmentRepo.findByAppointmentDateAndDoctorIdBetween(startDate, endDate ,doctorId);
    }

    // Belirli bir tarih aralığının içindeki ve belirli bir hayvana ait randevuları getirir.
    @Override
    public List<Appointment> findAppointmentsByDateRangeAndAnimal(LocalDateTime startDate, LocalDateTime endDate, Long animalId) {
        if (startDate == null || endDate == null || animalId == null || animalId <= 0) {
            throw new IllegalArgumentException("Geçersiz tarih veya hayvan ID");
        }
        return appointmentRepo.findByAppointmentDateAndAnimalIdBetween(startDate, endDate, animalId);
    }

    @Override
    public List<Appointment> findAppointmentsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        try {
            return appointmentRepo.findByAppointmentDateBetween(startDate, endDate);
        } catch (Exception e) {
            throw new RuntimeException("Randevular getirilirken bir hata oluştu", e);
        }
    }

    @Override
    public Page<Appointment> cursor(int page, int pageSize) {
        if (page < 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Geçersiz sayfa veya sayfa boyutu değerleri");
        }
        Pageable pageable = PageRequest.of(page, pageSize);
        return this.appointmentRepo.findAll(pageable);
    }
}