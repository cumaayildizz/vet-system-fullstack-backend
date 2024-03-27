package com.cuma.yildiz.VetSystem.api;

import com.cuma.yildiz.VetSystem.business.abstracts.IAnimalService;
import com.cuma.yildiz.VetSystem.business.abstracts.ICustomerService;
import com.cuma.yildiz.VetSystem.business.abstracts.IVaccineService;
import com.cuma.yildiz.VetSystem.core.config.modelMapper.IModelMapperService;
import com.cuma.yildiz.VetSystem.core.result.Result;
import com.cuma.yildiz.VetSystem.core.result.ResultData;
import com.cuma.yildiz.VetSystem.core.util.ResultHelper;
import com.cuma.yildiz.VetSystem.dto.request.animal.AnimalSaveRequest;
import com.cuma.yildiz.VetSystem.dto.request.animal.AnimalUpdateRequest;
import com.cuma.yildiz.VetSystem.dto.response.CursorResponse;
import com.cuma.yildiz.VetSystem.dto.response.entitiesResponse.AnimalResponse;
import com.cuma.yildiz.VetSystem.entities.Animal;
import com.cuma.yildiz.VetSystem.entities.Vaccine;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/vetSystem/animals")
public class AnimalController {

    private final IAnimalService animalService;
    private final ICustomerService customerService;
    private final IModelMapperService modelMapper;
    private final IVaccineService vaccineService;

    @Autowired
    public AnimalController(IAnimalService animalService, ICustomerService customerService,
                            IModelMapperService modelMapperService, IVaccineService vaccineService) {
        this.animalService = animalService;
        this.customerService = customerService;
        this.modelMapper = modelMapperService;
        this.vaccineService = vaccineService;
    }

    // Yeni bir hayvan kaydı oluşturma
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<AnimalResponse> save(@Valid @RequestBody AnimalSaveRequest animalSaveRequest) {
        Animal saveAnimal = this.modelMapper.forRequest().map(animalSaveRequest, Animal.class);
        saveAnimal.setId(null);

        this.animalService.saveAnimal(saveAnimal);

        AnimalResponse animalResponse = this.modelMapper.forResponse().map(saveAnimal, AnimalResponse.class);
        return ResultHelper.created(animalResponse);
    }

    // Belirli bir hayvanın bilgilerini getirme
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AnimalResponse> get(@PathVariable("id") long id) {
        Animal animal = this.animalService.getAnimalById(id);
        AnimalResponse animalResponse = this.modelMapper.forResponse().map(animal, AnimalResponse.class);
        return ResultHelper.success(animalResponse);
    }

    // Belirli bir müşteriye ait hayvanları getirme
    @GetMapping("/customer/{ownerId}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<AnimalResponse>> getAnimalsByOwner(@PathVariable("ownerId") long ownerId) {
        //Customer customer = customerService.getCustomerById(ownerId);
        List<Animal> animals = animalService.getAnimalsByCustomer(ownerId);
        List<AnimalResponse> animalResponses = animals.stream()
                .map(animal -> modelMapper.forResponse().map(animal, AnimalResponse.class))
                .collect(Collectors.toList());
        return ResultHelper.success(animalResponses);
    }

    // Tüm hayvanları getirme
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<AnimalResponse>> getAllAnimals() {
        List<Animal> animals = animalService.getAllAnimals();
        List<AnimalResponse> animalResponses = animals.stream()
                .map(animal -> modelMapper.forResponse().map(animal, AnimalResponse.class))
                .collect(Collectors.toList());
        return ResultHelper.success(animalResponses);
    }

    // İsme göre hayvanları filtreleme
    @GetMapping("/filter/{name}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<AnimalResponse>> getAnimalsByName(
            @PathVariable(name = "name", required = false) String name) {
        List<Animal> filteredAnimals = animalService.getAnimalsByName(name);
        List<AnimalResponse> animalResponses = filteredAnimals.stream()
                        .map(animal -> modelMapper.forResponse().map(animal, AnimalResponse.class))
                        .collect(Collectors.toList());
        return ResultHelper.success(animalResponses);
    }

    //Safa veri veri sayisina gore filtreleme
    @GetMapping("/cursor")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CursorResponse<AnimalResponse>> cursor(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "2") int pageSize) {
        Page<Animal> animalPage = this.animalService.cursor(page, pageSize);
        Page<AnimalResponse> animalResponsePage = animalPage
                .map(animal -> this.modelMapper.forResponse().map(animal, AnimalResponse.class));

        return ResultHelper.cursorMethod(animalResponsePage);
    }

    // Hayvan bilgilerini güncelleme
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<AnimalResponse> update(@Valid @RequestBody AnimalUpdateRequest animalUpdateRequest) {
        this.animalService.getAnimalById(animalUpdateRequest.getId());
        Animal updateAnimal = modelMapper.forRequest().map(animalUpdateRequest, Animal.class);
        animalService.updateAnimal(updateAnimal);
        AnimalResponse animalResponse = modelMapper.forResponse().map(updateAnimal, AnimalResponse.class);
        return ResultHelper.updated(animalResponse);
    }

    @GetMapping("/gender/{gender}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<AnimalResponse>> getAnimalsByGender(@PathVariable("gender") String gender) {
        List<Animal> animals = animalService.getAnimalsByGender(gender);
        List<AnimalResponse> animalResponses = animals.stream()
                .map(animal -> modelMapper.forResponse().map(animal, AnimalResponse.class))
                .collect(Collectors.toList());
        return ResultHelper.success(animalResponses);
    }

    @GetMapping("/birthdate")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<AnimalResponse>> getAnimalsByDateOfBirthBetween(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Animal> animals = animalService.getAnimalsByDateOfBirthBetween(startDate, endDate);
        List<AnimalResponse> animalResponses = animals.stream()
                .map(animal -> modelMapper.forResponse().map(animal, AnimalResponse.class))
                .collect(Collectors.toList());
        return ResultHelper.success(animalResponses);
    }

    // Belirli bir hayvana ait tüm aşı kayıtlarını getirme
    @GetMapping("/{animalId}/vaccines")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<Vaccine>> getVaccinesByAnimal(@PathVariable("animalId") long animalId) {
        List<Vaccine> vaccines = vaccineService.getVaccinesByAnimalId(animalId);
        return ResultHelper.success(vaccines);
    }

    // Hayvanları sahibine göre listeleme
    @GetMapping("/ownerName/{ownerName}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<AnimalResponse>> getAnimalsByOwnerName(@PathVariable("ownerName") String ownerName) {
        List<Animal> animals = animalService.getAnimalsByCustomerName(ownerName);
        List<AnimalResponse> animalResponses = animals.stream()
                .map(animal -> modelMapper.forResponse().map(animal, AnimalResponse.class))
                .collect(Collectors.toList());
        return ResultHelper.success(animalResponses);
    }

    // Hayvanları adına göre listeleme. Buuk kucuk harf duyarliligi var.m
    @GetMapping("/animalName/{animalName}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<AnimalResponse>> getAnimalsByILIKEName(@PathVariable("animalName") String animalName) {
        List<Animal> animals = animalService.getAnimalsByName(animalName);
        List<AnimalResponse> animalResponses = animals.stream()
                .map(animal -> modelMapper.forResponse().map(animal, AnimalResponse.class))
                .collect(Collectors.toList());
        return ResultHelper.success(animalResponses);
    }

    // Belirli bir hayvanı silme
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable("id") long id) {
        this.animalService.deleteAnimal(id);
        return ResultHelper.deleted();
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<AnimalResponse>> searchAnimals(
            @RequestParam("name") String name,
            @RequestParam("species") String species,
            @RequestParam("breed") String breed) {

        List<Animal> animals = animalService.findAnimalsByNameAndSpeciesAndBreed(name, species, breed);
        List<AnimalResponse> animalResponses = animals.stream()
                .map(animal -> modelMapper.forResponse().map(animal, AnimalResponse.class))
                .collect(Collectors.toList());

        return ResultHelper.success(animalResponses);
    }
}
