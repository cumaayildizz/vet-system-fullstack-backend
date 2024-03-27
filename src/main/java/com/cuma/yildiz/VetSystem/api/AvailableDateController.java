package com.cuma.yildiz.VetSystem.api;

import com.cuma.yildiz.VetSystem.business.abstracts.IAvailableDateService;
import com.cuma.yildiz.VetSystem.business.abstracts.IDoctorService;
import com.cuma.yildiz.VetSystem.core.config.modelMapper.IModelMapperService;
import com.cuma.yildiz.VetSystem.core.result.Result;
import com.cuma.yildiz.VetSystem.core.result.ResultData;
import com.cuma.yildiz.VetSystem.core.util.ResultHelper;
import com.cuma.yildiz.VetSystem.dto.request.availiableDate.AvailableDateSaveRequest;
import com.cuma.yildiz.VetSystem.dto.request.availiableDate.AvailableDateUpdateRequest;
import com.cuma.yildiz.VetSystem.dto.response.CursorResponse;
import com.cuma.yildiz.VetSystem.dto.response.entitiesResponse.AvailableDateResponse;
import com.cuma.yildiz.VetSystem.entities.AvailableDate;
import com.cuma.yildiz.VetSystem.entities.Doctor;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/vetSystem/available-dates")
public class AvailableDateController {
    private final IAvailableDateService availableDateService;
    private final IDoctorService doctorService;
    private final IModelMapperService modelMapper;

    public AvailableDateController(IAvailableDateService availableDateService, IDoctorService doctorService, IModelMapperService modelMapperService) {
        this.availableDateService = availableDateService;
        this.doctorService = doctorService;
        this.modelMapper = modelMapperService;
    }

    // Yeni bir AvailableDate kaydeden metod.
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<AvailableDateResponse> save(@Valid @RequestBody AvailableDateSaveRequest availableDateSaveRequest) {
        // Gelen request'i AvailableDate sınıfına map et
        AvailableDate saveAvailableDate = this.modelMapper.forRequest().map(availableDateSaveRequest, AvailableDate.class);

        saveAvailableDate.setId(null); //*********

        // AvailableDate'i kaydet
        availableDateService.saveAvailableDate(saveAvailableDate);

        // Kaydedilen AvailableDate'i response sınıfına map et ve başarılı response döndür
        AvailableDateResponse availableDateResponse = this.modelMapper.forResponse().map(saveAvailableDate, AvailableDateResponse.class);
        return ResultHelper.created(availableDateResponse);
    }

    // Tüm AvailableDate kayıtlarını getiren metod
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<AvailableDateResponse>> getAppointments() {
        // Tüm AvailableDate kayıtlarını getir
        List<AvailableDate> availableDates = availableDateService.getAllAvailableDates();

        // Elde edilen kayıtları response sınıfına map et ve başarılı response döndür
        List<AvailableDateResponse> availableDateResponses = availableDates.stream()
                .map(availableDate -> modelMapper.forResponse().map(availableDate, AvailableDateResponse.class))
                .collect(Collectors.toList());
        return ResultHelper.success(availableDateResponses);
    }

    // Sayfalı AvailableDate kayıtlarını getiren metod
    @GetMapping("/cursor")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CursorResponse<AvailableDateResponse>> cursor(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "2") int pageSize) {
        // Sayfalı AvailableDate kayıtlarını getir
        Page<AvailableDate> availableDatePage = this.availableDateService.cursor(page, pageSize);
        Page<AvailableDateResponse> availableDateResponsePage = availableDatePage
                .map(availableDate -> this.modelMapper.forResponse()
                        .map(availableDate, AvailableDateResponse.class));

        // Elde edilen sayfalı kayıtları response sınıfına map et ve başarılı response döndür
        return ResultHelper.cursorMethod(availableDateResponsePage);
    }

    // Belirli bir AvailableDate kaydını getiren metod
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AvailableDateResponse> get(@PathVariable("id") long id) {
        // Belirli bir AvailableDate kaydını getir
        AvailableDate availableDate = availableDateService.getAvailableDateById(id);

        // Elde edilen kaydı response sınıfına map et ve başarılı response döndür
        AvailableDateResponse availableDateResponse = this.modelMapper.forResponse().map(availableDate, AvailableDateResponse.class);
        return ResultHelper.success(availableDateResponse);
    }

    // Belirli bir doktora ait AvailableDate kayıtlarını getiren metod
    @GetMapping("/doctor/{doctorId}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<AvailableDateResponse>> getAvailableDatesByDoctor(@PathVariable("doctorId") long doctorId) {
        // Belirli bir doktora ait AvailableDate kayıtlarını getir
        List<AvailableDate> availableDates = availableDateService.getAvailableDatesByDoctorId(doctorId);

        // Elde edilen kayıtları response sınıfına map et ve başarılı response döndür
        List<AvailableDateResponse> availableDateResponses = availableDates.stream()
                .map(availableDate -> modelMapper.forResponse().map(availableDate, AvailableDateResponse.class))
                .collect(Collectors.toList());
        return ResultHelper.success(availableDateResponses);
    }

    // Belirli bir AvailableDate kaydını güncelleyen metod
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<AvailableDateResponse> update(
            @PathVariable("id") long id,
            @Valid @RequestBody AvailableDateUpdateRequest availableDateUpdateRequest) {
        this.availableDateService.getAvailableDateById(availableDateUpdateRequest.getId());
        // Güncellenecek AvailableDate kaydını request sınıfından map et
        AvailableDate updateAvailableDate = modelMapper.forRequest().map(availableDateUpdateRequest, AvailableDate.class);
        updateAvailableDate.setId(id);

        // Güncellenen AvailableDate kaydını kaydet
        availableDateService.updateAvailableDate(updateAvailableDate);

        // Güncellenen AvailableDate kaydını response sınıfına map et ve başarılı response döndür
        AvailableDateResponse availableDateResponse = modelMapper.forResponse().map(updateAvailableDate, AvailableDateResponse.class);
        return ResultHelper.updated(availableDateResponse);
    }

    // Belirli bir doktorun belirli bir tarihte müsait olup olmadığını kontrol eden metod
    @GetMapping("/doctor/{doctorId}/availability")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<String> checkDoctorAvailability(
            @PathVariable("doctorId") Long doctorId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        // Belirli bir doktorun belirtilen tarihte müsait olup olmadığını kontrol et
        boolean isAvailable = availableDateService.isDoctorAvailableOnDate(doctorId, date);

        // Kontrol sonucuna göre başarılı bir response döndür
        if (isAvailable) {
            return ResultHelper.success("Doktor belirtilen tarihte müsaittir.");
        } else {
            return ResultHelper.success("Doktor belirtilen tarihte müsait değil.");
        }
    }

    // Belirli bir doktorun belirli bir tarih aralığındaki müsait zamanlarını getiren metod
    @GetMapping("/doctor/{doctorId}/availability-range")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<AvailableDateResponse>> getAvailableDatesByDoctorAndDateRange(
            @PathVariable("doctorId") long doctorId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        // Belirli bir doktorun belirli bir tarih aralığındaki müsait zamanlarını getir
        List<AvailableDate> availableDates = availableDateService.getAvailableDatesByDoctorIdAndDateRange(doctorId, startDate, endDate);

        // Elde edilen kayıtları response sınıfına map et ve başarılı response döndür
        List<AvailableDateResponse> availableDateResponses = availableDates.stream()
                .map(availableDate -> modelMapper.forResponse().map(availableDate, AvailableDateResponse.class))
                .collect(Collectors.toList());
        return ResultHelper.success(availableDateResponses);
    }

    // Belirli bir AvailableDate kaydını silen metod
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable("id") long id) {
        availableDateService.deleteAvailableDate(id);
        return ResultHelper.deleted();
    }
}
