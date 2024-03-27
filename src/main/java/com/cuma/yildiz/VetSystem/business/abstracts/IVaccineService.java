package com.cuma.yildiz.VetSystem.business.abstracts;

import com.cuma.yildiz.VetSystem.dto.request.appointment.AppointmentSaveRequest;
import com.cuma.yildiz.VetSystem.entities.Animal;
import com.cuma.yildiz.VetSystem.entities.Customer;
import com.cuma.yildiz.VetSystem.entities.Vaccine;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface IVaccineService {

    // Yeni bir aşı kaydı oluşturma
    Vaccine saveVaccine(Vaccine vaccine);

    // Aşı kaydını güncelleme
    Vaccine updateVaccine(Vaccine vaccine);

    // Aşı kaydını silme
    boolean delete(long id);

    // Belirli bir aşı kaydını getirme
    Vaccine getVaccineById(Long id);

    // Belirli bir hayvana ait tüm aşı kayıtlarını getirme
    List<Vaccine> getVaccinesByAnimalId(Long animalId);


    // Tüm aşı kayıtlarını getirme
    List<Vaccine> getAllVaccines();

    // Belirli bir tarih aralığına göre koruma başlangıcı olan aşı kayıtlarını getirme
    List<Animal> getAnimalsWithUpcomingVaccines(LocalDate startDate, LocalDate endDate);

    // Belirli bir tarih aralığına göre aşı kayıtlarını getirme
    List<Vaccine> getVaccinesByDateRange(LocalDate startDate, LocalDate endDate);

    // Belirli bir hayvana ait, belirli bir tarih aralığına göre koruma başlangıcı olan aşı kayıtlarını getirme
    List<Vaccine> findByAnimalIdAndProtectionStartDateBetween(Long animalId, LocalDate startDate, LocalDate endDate);

    // Sayfalama işlemi için aşı kayıtlarını getirme
    Page<Vaccine> cursor(int page , int pageSize);

}

//List<Vaccine> getVaccinesWithExpiredProtection(LocalDate currentDate);
// boolean createAppointmentWithAvailabilityCheck(AppointmentSaveRequest appointmentSaveRequest);