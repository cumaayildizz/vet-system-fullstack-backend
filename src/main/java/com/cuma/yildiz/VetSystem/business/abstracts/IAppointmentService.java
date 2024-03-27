package com.cuma.yildiz.VetSystem.business.abstracts;

import com.cuma.yildiz.VetSystem.entities.Appointment;
import com.cuma.yildiz.VetSystem.entities.Doctor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface IAppointmentService {
    // Yeni randevu kaydı oluşturma
    Appointment saveAppointment(Appointment appointment);

    // ID'ye göre randevu getirme
    Appointment getAppointmentById(Long id);

    // Tüm randevuları getirme
    List<Appointment> getAllAppointments();

    // Randevu bilgilerini güncelleme
    Appointment updateAppointment(Appointment appointment);

    // ID'ye göre randevu silme
    void deleteAppointment(Long id);

    // Doktor ID'sine göre randevuları bulma
    List<Appointment> findAppointmentsByDoctor(Long doctorId);

    List<Appointment> findAppointmentsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    // Hayvan ID'sine göre randevuları bulma
    List<Appointment> findAppointmentsByAnimal(Long animalId);

    List<Appointment>findByAppointmentDateAndAnimalIdAndDoctorIdBetween
            (LocalDateTime startDate, LocalDateTime endDate, Long animalId , Long doctorId);

    List<Appointment>findByAppointmentDateAndDoctorIdBetween
            (LocalDateTime startDate, LocalDateTime endDate, Long doctorId);

    // Tarih ve hayvan ID'sine göre randevuları bulma
    List<Appointment> findAppointmentsByDateRangeAndAnimal(LocalDateTime startDate, LocalDateTime endDate, Long animalId);

    // Sayfalı randevu listesi getirme
    Page<Appointment> cursor(int page, int pageSize);
}

/*private void checkValidedAppointment(Appointment appointment) {
        AvailableDate checkAvailableDateOfDoctor = availableDateManager
                .findByAvailableDateAndDoctorId(
                        appointment.getAppointmentDate().toLocalDate(),
                        appointment.getDoctor().getId()
                )
                .orElseThrow(() -> new RuntimeException("Bu tarihte doktorun uygun saati bulunmuyor."));

        Optional<Appointment> checkAppointment = appointmentRepo.findByAppointmentDateAndDoctorId(
                appointment.getAppointmentDate(),
                appointment.getDoctor().getId()
        );
        if (checkAppointment.isPresent()) {
            throw new RuntimeException("Bu tarihte doktorun uygun saati bulunmuyor.");
        }
    }// appoinment manager sınıfında kullanacağımız metotları yazıyoruz.*/
