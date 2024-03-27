package com.cuma.yildiz.VetSystem.api;

import com.cuma.yildiz.VetSystem.business.abstracts.IAnimalService;
import com.cuma.yildiz.VetSystem.business.abstracts.IVaccineService;
import com.cuma.yildiz.VetSystem.core.config.modelMapper.IModelMapperService;
import com.cuma.yildiz.VetSystem.core.result.Result;
import com.cuma.yildiz.VetSystem.core.result.ResultData;
import com.cuma.yildiz.VetSystem.core.util.ResultHelper;
import com.cuma.yildiz.VetSystem.dto.request.vaccine.VaccineSaveRequest;
import com.cuma.yildiz.VetSystem.dto.request.vaccine.VaccineUpdateRequest;
import com.cuma.yildiz.VetSystem.dto.response.CursorResponse;
import com.cuma.yildiz.VetSystem.dto.response.entitiesResponse.AnimalResponse;
import com.cuma.yildiz.VetSystem.dto.response.entitiesResponse.VaccineResponse;
import com.cuma.yildiz.VetSystem.entities.Animal;
import com.cuma.yildiz.VetSystem.entities.Vaccine;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/vetSystem/vaccines")
public class VaccineController {
    private final IVaccineService vaccineService;
    private final IAnimalService animalService;
    private final IModelMapperService modelMapper;

    public VaccineController(IVaccineService vaccineService, IAnimalService animalService, IModelMapperService modelMapperService) {
        this.vaccineService = vaccineService;
        this.animalService = animalService;
        this.modelMapper = modelMapperService;
    }

    // Yeni bir aşı kaydı oluşturur.
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<VaccineResponse> save(@Valid @RequestBody VaccineSaveRequest vaccineSaveRequest){
        Vaccine saveVaccine = this.modelMapper.forRequest().map(vaccineSaveRequest, Vaccine.class);
        saveVaccine.setId(null);

        this.vaccineService.saveVaccine(saveVaccine);

        VaccineResponse vaccineResponse = this.modelMapper.forResponse().map(saveVaccine, VaccineResponse.class);
        return ResultHelper.created(vaccineResponse);
    }

    // Belirli bir aşı kaydını getirir.
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<VaccineResponse> get(@PathVariable("id") long id){
        Vaccine vaccine = this.vaccineService.getVaccineById(id);
        VaccineResponse vaccineResponse = this.modelMapper.forResponse().map(vaccine, VaccineResponse.class);
        return ResultHelper.success(vaccineResponse);
    }

    // Tüm aşı kayıtlarını getirir.
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<VaccineResponse>> getVaccines() {
        List<Vaccine> vaccines = vaccineService.getAllVaccines();
        List<VaccineResponse> vaccineResponses = vaccines.stream()
                .map(vaccine -> modelMapper.forResponse().map(vaccine, VaccineResponse.class))
                .collect(Collectors.toList());
        return ResultHelper.success(vaccineResponses);
    }

    // Sayfalı bir şekilde aşı kayıtlarını getirir.
    @GetMapping("/cursor")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CursorResponse<VaccineResponse>> cursor(
            @RequestParam(name = "page" , required = false , defaultValue = "0") int page ,
            @RequestParam(name = "pageSize" , required = false , defaultValue = "2") int pageSize)
    {
        Page<Vaccine> vaccinePage = this.vaccineService.cursor(page , pageSize);
        Page<VaccineResponse> vaccineResponsePage = vaccinePage
                .map(vaccine -> this.modelMapper.forResponse()
                        .map(vaccine , VaccineResponse.class));

        return ResultHelper.cursorMethod(vaccineResponsePage);
    }

    // Belirli bir hayvanın aşı kayıtlarını getirir.
    @GetMapping("/animals/{animalId}/vaccines")
    public ResultData<List<VaccineResponse>> getVaccinesByAnimalId(@PathVariable("animalId") long animalId) {
        List<Vaccine> vaccines = vaccineService.getVaccinesByAnimalId(animalId);
        List<VaccineResponse> vaccineResponses = vaccines.stream()
                .map(vaccine -> modelMapper.forResponse().map(vaccine, VaccineResponse.class))
                .collect(Collectors.toList());
        return ResultHelper.success(vaccineResponses);
    }

    // Belirli bir tarih aralığındaki aşı kayıtlarını getirir.
    @GetMapping("/date-range")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<VaccineResponse>> getVaccinesByDateRange(
            @RequestParam("start-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("end-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Vaccine> vaccines = vaccineService.getVaccinesByDateRange(startDate, endDate);
        List<VaccineResponse> vaccineResponses = vaccines.stream()
                .map(vaccine -> modelMapper.forResponse().map(vaccine, VaccineResponse.class))
                .collect(Collectors.toList());

        return ResultHelper.success(vaccineResponses);
    }

    // Belirli bir aşı kaydını günceller.
    @PutMapping("/{id}")
    public ResultData<VaccineResponse> updateVaccine(
            @PathVariable("id") long vaccineId,
            @Valid @RequestBody VaccineUpdateRequest vaccineUpdateRequest) {

        this.vaccineService.getVaccineById(vaccineUpdateRequest.getId());
        Vaccine existingVaccine = vaccineService.getVaccineById(vaccineId);
        modelMapper.forRequest().map(vaccineUpdateRequest, existingVaccine);
        Vaccine updatedVaccine = vaccineService.updateVaccine(existingVaccine);
        VaccineResponse vaccineResponse = modelMapper.forResponse().map(updatedVaccine, VaccineResponse.class);
        return ResultHelper.success(vaccineResponse);
    }

    // Belirli bir tarih aralığındaki gelecekteki aşıları olan hayvanları getirir.
    @GetMapping("/upcoming-vaccines")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<AnimalResponse>> getAnimalsWithUpcomingVaccines(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Animal> animals = vaccineService.getAnimalsWithUpcomingVaccines(startDate, endDate);
        List<AnimalResponse> animalResponses = animals.stream()
                .map(animal -> modelMapper.forResponse().map(animal, AnimalResponse.class))
                .collect(Collectors.toList());

        return ResultHelper.success(animalResponses);
    }

    // Belirli bir hayvanın belirli bir tarih aralığındaki aşı bilgilerini getirir.
    @GetMapping("/animal/filter/date")
    public ResultData<List<VaccineResponse>> getVaccinesByAnimalAndDateRange(
            @RequestParam("animalId") Long animalId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Vaccine> vaccines = vaccineService.findByAnimalIdAndProtectionStartDateBetween(animalId, startDate, endDate);
        List<VaccineResponse> vaccineResponses = vaccines.stream()
                .map(vaccine -> modelMapper.forResponse().map(vaccine, VaccineResponse.class))
                .collect(Collectors.toList());

        return ResultHelper.success(vaccineResponses);
    }


    // Belirli bir aşı kaydını siler.
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable("id") long id){
        this.vaccineService.delete(id);
        return ResultHelper.Ok();
    }
}

