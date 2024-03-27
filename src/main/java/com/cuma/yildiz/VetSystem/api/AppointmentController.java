package com.cuma.yildiz.VetSystem.api;

import com.cuma.yildiz.VetSystem.business.abstracts.IAnimalService;
import com.cuma.yildiz.VetSystem.business.abstracts.IAppointmentService;
import com.cuma.yildiz.VetSystem.business.abstracts.IDoctorService;
import com.cuma.yildiz.VetSystem.core.config.modelMapper.IModelMapperService;
import com.cuma.yildiz.VetSystem.core.result.Result;
import com.cuma.yildiz.VetSystem.core.result.ResultData;
import com.cuma.yildiz.VetSystem.core.util.ResultHelper;
import com.cuma.yildiz.VetSystem.dto.request.appointment.AppointmentSaveRequest;
import com.cuma.yildiz.VetSystem.dto.request.appointment.AppointmentUpdateRequest;
import com.cuma.yildiz.VetSystem.dto.response.CursorResponse;
import com.cuma.yildiz.VetSystem.dto.response.entitiesResponse.AppointmentResponse;
import com.cuma.yildiz.VetSystem.entities.Appointment;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/vetSystem/appointments")
public class AppointmentController {
    private final IAppointmentService appointmentService;
    private final IAnimalService animalService;
    private final IDoctorService doctorService;
    private final IModelMapperService modelMapper;

    public AppointmentController(IAppointmentService appointmentService, IAnimalService animalService, IDoctorService doctorService, IModelMapperService modelMapperService) {
        this.appointmentService = appointmentService;
        this.animalService = animalService;
        this.doctorService = doctorService;
        this.modelMapper = modelMapperService;
    }

    // Yeni bir randevu kaydı oluşturma
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<AppointmentResponse> save(@Valid @RequestBody AppointmentSaveRequest appointmentSaveRequest){
        Appointment saveAppointment = this.modelMapper.forRequest().map(appointmentSaveRequest, Appointment.class);

        /*Doctor doctor = this.doctorService.getDoctorById(appointmentSaveRequest.getDoctorId());
        saveAppointment.setDoctor(doctor);

        Animal animal = this.animalService.getAnimalById(appointmentSaveRequest.getAnimalId());
        saveAppointment.setAnimal(animal);*/

        this.appointmentService.saveAppointment(saveAppointment);

        AppointmentResponse appointmentResponse = this.modelMapper.forResponse().map(saveAppointment, AppointmentResponse.class);
        return ResultHelper.created(appointmentResponse);
    }

    // Tüm randevuları getirme
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<AppointmentResponse>> getAppointments() {
        List<Appointment> appointments = appointmentService.getAllAppointments();

        //appoıntment lıstesını gezerken her buldugun her appoıntment ıcın maplama yap
        //Bulduğun her appoıntment nesnesını AppointmentResponse maple
        //En son maplediğin tüm verileri topla(.collect) ve bir listeye çevir(Collectors.toList());
        //Stream listelerde kullanılır
        List<AppointmentResponse> appointmentResponses = appointments.stream()
                .map(appointment -> modelMapper.forResponse().map(appointment, AppointmentResponse.class))
                .collect(Collectors.toList());
        return ResultHelper.success(appointmentResponses);
    }

    // Sayfalı randevu listesi getirme
    @GetMapping("/cursor")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CursorResponse<AppointmentResponse>> cursor(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "2") int pageSize) {
        Page<Appointment> appointmentPage = this.appointmentService.cursor(page, pageSize);
        Page<AppointmentResponse> appointmentResponsePage = appointmentPage
                .map(author -> this.modelMapper.forResponse()
                        .map(author, AppointmentResponse.class));

        return ResultHelper.cursorMethod(appointmentResponsePage);
    }

    // ID'ye göre randevu getirme
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AppointmentResponse> get(@PathVariable("id") long id) {
        Appointment appointment = this.appointmentService.getAppointmentById(id);
        AppointmentResponse appointmentResponse = this.modelMapper.forResponse().map(appointment, AppointmentResponse.class);
        return ResultHelper.success(appointmentResponse);
    }

    // Randevu bilgilerini güncelleme
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<AppointmentResponse> supdate(@PathVariable("id") long id, @Valid @RequestBody AppointmentUpdateRequest appointmentUpdateRequest) {
        this.appointmentService.getAppointmentById(appointmentUpdateRequest.getId());
        Appointment updateAppointment = modelMapper.forRequest().map(appointmentUpdateRequest, Appointment.class);
        updateAppointment.setId(id);
        appointmentService.updateAppointment(updateAppointment);

        AppointmentResponse appointmentResponse = modelMapper.forResponse().map(updateAppointment, AppointmentResponse.class);
        return ResultHelper.updated(appointmentResponse);
    }

    // ID'ye göre randevu silme
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable("id") long id) {
        this.appointmentService.deleteAppointment(id);
        return ResultHelper.deleted();
    }

    // Tarih aralığına ve hayvan ID'sine göre randevuları getirme
    @GetMapping("/filter/animal/date-range")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<AppointmentResponse>> getAppointmentsByDateAndAnimal(
            @RequestParam("animalId") Long animalId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<Appointment> appointments = appointmentService.findAppointmentsByDateRangeAndAnimal(startDate, endDate, animalId);
        List<AppointmentResponse> appointmentResponses = appointments.stream()
                .map(appointment -> modelMapper.forResponse().map(appointment, AppointmentResponse.class))
                .collect(Collectors.toList());

        return ResultHelper.success(appointmentResponses);
    }

    // Tarih aralığına , Doktor ID'sine ve hayvan ID'sine göre randevuları getirme
    @GetMapping("/filter/animal/doctor/date-range")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<AppointmentResponse>> getAppointmentsByDateAndAnimalAndDoctor(
            @RequestParam("animalId") Long animalId,
            @RequestParam("doctorId") Long doctorId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<Appointment> appointments = appointmentService
                .findByAppointmentDateAndAnimalIdAndDoctorIdBetween(startDate, endDate, animalId ,doctorId);
        List<AppointmentResponse> appointmentResponses = appointments.stream()
                .map(appointment -> modelMapper.forResponse().map(appointment, AppointmentResponse.class))
                .collect(Collectors.toList());

        return ResultHelper.success(appointmentResponses);
    }

    // Tarih aralığına , Doktor ID'sine ve hayvan ID'sine göre randevuları getirme
    @GetMapping("/filter/doctor/date-range")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<AppointmentResponse>> getAppointmentsByDateAndDoctorFilter(
            @RequestParam("doctorId") Long doctorId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<Appointment> appointments = appointmentService
                .findByAppointmentDateAndDoctorIdBetween(startDate, endDate ,doctorId);
        List<AppointmentResponse> appointmentResponses = appointments.stream()
                .map(appointment -> modelMapper.forResponse().map(appointment, AppointmentResponse.class))
                .collect(Collectors.toList());

        return ResultHelper.success(appointmentResponses);
    }


    // Hayvan ID'sine göre randevuları getirme
    @GetMapping("/filter/animal/{animalId}")
    public ResultData<List<AppointmentResponse>> getAppointmentsByAnimalId(@PathVariable("animalId") long animalId) {
        List<Appointment> appointments = appointmentService.findAppointmentsByAnimal(animalId);
        List<AppointmentResponse> appointmentResponses = appointments.stream()
                .map(appointment -> modelMapper.forResponse().map(appointment, AppointmentResponse.class))
                .collect(Collectors.toList());
        return ResultHelper.success(appointmentResponses);
    }

    // Doktor ID'sine göre randevuları getirme
    @GetMapping("/filter/doctor/{doctorId}")
    public ResultData<List<AppointmentResponse>> getAppointmentsByDoctorId(@PathVariable("doctorId") long doctorId) {
        List<Appointment> appointments = appointmentService.findAppointmentsByDoctor(doctorId);
        List<AppointmentResponse> appointmentResponses = appointments.stream()
                .map(appointment -> modelMapper.forResponse().map(appointment, AppointmentResponse.class))
                .collect(Collectors.toList());
        return ResultHelper.success(appointmentResponses);
    }


    // Tarih aralığına göre randevuları getirme
    @GetMapping("/filter/date")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<AppointmentResponse>> getAppointmentsByDateRange(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<Appointment> appointments = appointmentService.findAppointmentsByDateRange(startDate, endDate);
        List<AppointmentResponse> appointmentResponses = appointments.stream()
                .map(appointment -> modelMapper.forResponse().map(appointment, AppointmentResponse.class))
                .collect(Collectors.toList());

        return ResultHelper.success(appointmentResponses);
    }
}
