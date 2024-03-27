package com.cuma.yildiz.VetSystem.api;

import com.cuma.yildiz.VetSystem.business.abstracts.ICustomerService;
import com.cuma.yildiz.VetSystem.core.config.modelMapper.IModelMapperService;
import com.cuma.yildiz.VetSystem.core.result.Result;
import com.cuma.yildiz.VetSystem.core.result.ResultData;
import com.cuma.yildiz.VetSystem.core.util.ResultHelper;
import com.cuma.yildiz.VetSystem.dto.request.customer.CustomerSaveRequest;
import com.cuma.yildiz.VetSystem.dto.request.customer.CustomerUpdateRequest;
import com.cuma.yildiz.VetSystem.dto.response.CursorResponse;
import com.cuma.yildiz.VetSystem.dto.response.entitiesResponse.AnimalResponse;
import com.cuma.yildiz.VetSystem.dto.response.entitiesResponse.CustomerResponse;
import com.cuma.yildiz.VetSystem.entities.Animal;
import com.cuma.yildiz.VetSystem.entities.Customer;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/vetSystem/customers")
@CrossOrigin(origins = "http://localhost:5173")
public class CustomerController {
    private final ICustomerService customerService;
    private final IModelMapperService modelMapper;

    public CustomerController(ICustomerService customerService, IModelMapperService modelMapper) {
        this.customerService = customerService;
        this.modelMapper = modelMapper;
    }

    // Yeni müşteri kaydı oluştur
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<CustomerResponse> save(@Valid @RequestBody CustomerSaveRequest customerSaveRequest) {
        Customer saveCustomer = modelMapper.forRequest().map(customerSaveRequest, Customer.class);
        customerService.saveCustomer(saveCustomer);
        CustomerResponse customerResponse = modelMapper.forResponse().map(saveCustomer, CustomerResponse.class);
        return ResultHelper.created(customerResponse);
    }

    // Belirli bir müşteriyi getir
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CustomerResponse> get(@PathVariable("id") long id) {
        Customer customer = customerService.getCustomerById(id);
        CustomerResponse customerResponse = modelMapper.forResponse().map(customer, CustomerResponse.class);
        return ResultHelper.success(customerResponse);
    }

    // Tüm müşterileri getir
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<CustomerResponse>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        List<CustomerResponse> customerResponses = customers.stream()
                .map(customer -> modelMapper.forResponse().map(customer, CustomerResponse.class))
                .collect(Collectors.toList());
        return ResultHelper.success(customerResponses);
    }

    // Sayfalı müşteri listesi
    @GetMapping("/cursor")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CursorResponse<CustomerResponse>> cursor(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "2") int pageSize) {
        Page<Customer> customerPage = this.customerService.cursor(page, pageSize);
        Page<CustomerResponse> customerResponsePage = customerPage
                .map(customer -> this.modelMapper.forResponse()
                        .map(customer, CustomerResponse.class));

        return ResultHelper.cursorMethod(customerResponsePage);
    }

    // İsim ile müşteri ara
    @GetMapping("/name/{name}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<CustomerResponse>> getCustomersByName(
            @PathVariable(name = "name", required = false) String name) {
        List<Customer> filteredCustomers = customerService.getCustomersByName(name);
        List<CustomerResponse> customerResponses = filteredCustomers.stream()
                .map(customer -> modelMapper.forResponse().map(customer, CustomerResponse.class))
                .collect(Collectors.toList());
        return ResultHelper.success(customerResponses);
    }

    // Müşteri güncelle
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<CustomerResponse> update(@Valid @RequestBody CustomerUpdateRequest customerUpdateRequest) {
        this.customerService.getCustomerById(customerUpdateRequest.getId());
        Customer updateCustomer = modelMapper.forRequest().map(customerUpdateRequest, Customer.class);
        customerService.updateCustomer(updateCustomer);
        CustomerResponse customerResponse = modelMapper.forResponse().map(updateCustomer, CustomerResponse.class);
        return ResultHelper.updated(customerResponse);
    }

    // Belirli bir müşterinin hayvanlarını getir
    @GetMapping("/{customerId}/animals")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<AnimalResponse>> getCustomerAnimals(@PathVariable("customerId") Long customerId) {
        List<Animal> customerAnimals = customerService.getAnimalsByCustomerId(customerId);
        List<AnimalResponse> animalResponses = customerAnimals.stream()
                .map(animal -> modelMapper.forResponse().map(animal, AnimalResponse.class))
                .collect(Collectors.toList());
        return ResultHelper.success(animalResponses);
    }

    // Belirli bir müşteriyi sil
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable("id") long id) {
        customerService.deleteCustomer(id);
        return ResultHelper.deleted();
    }
}