package com.cuma.yildiz.VetSystem.business.concretes;

import com.cuma.yildiz.VetSystem.business.abstracts.ICustomerService;
import com.cuma.yildiz.VetSystem.core.config.exceptions.NotFoundException;
import com.cuma.yildiz.VetSystem.core.util.Messages;
import com.cuma.yildiz.VetSystem.dao.CustomerRepo;
import com.cuma.yildiz.VetSystem.entities.Animal;
import com.cuma.yildiz.VetSystem.entities.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerManager implements ICustomerService {
    private final CustomerRepo customerRepo;

    public CustomerManager(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        // Müşteri adı boş olamaz
        if (customer.getName() == null || customer.getName().isEmpty()) {
            throw new NotFoundException(Messages.CUSTOMER_NAME_NOT_FOUND);
        }
        return customerRepo.save(customer);
    }

    @Override
    public Customer getCustomerById(Long id) {
        // Müşteri bulunamazsa NotFoundException fırlat
        return customerRepo.findById(id).orElseThrow(() -> new NotFoundException(Messages.NOT_FOUND));
    }

    @Override
    public List<Customer> getAllCustomers() {
        // Müşteri bulunamazsa RuntimeException fırlat
        List<Customer> customers = customerRepo.findAll();
        if (customers == null || customers.isEmpty()) {
            throw new NotFoundException(Messages.NOT_FOUND);
        }
        return customers;
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        // Müşteri bulunamazsa NotFoundException fırlat
        getCustomerById(customer.getId());

        // Müşteri ID boş olamaz
        if (customer.getId() == null) {
            throw new RuntimeException(Messages.CUSTOMER_ID_NOT_FOUND);
        }

        // Müşteri adı boş olamaz
        if (customer.getName() == null || customer.getName().isEmpty()) {
            throw new NotFoundException(Messages.CUSTOMER_NAME_NOT_FOUND);
        }

        return customerRepo.save(customer);
    }

    @Override
    public void deleteCustomer(Long id) {
        // Müşteri bulunamazsa NotFoundException fırlat
        getCustomerById(id);

        // ID geçersiz veya negatifse IllegalArgumentException fırlat
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(id + Messages.NEGATIVE_ID);
        }

        customerRepo.deleteById(id);
    }

    @Override
    public List<Customer> getCustomersByName(String name) {
        // Müşteri adı boş olamaz
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException(Messages.CUSTOMER_NAME_NOT_FOUND);
        }
        return customerRepo.findByName(name);
    }

    @Override
    public List<Animal> getAnimalsByCustomerId(Long customerId) {
        // Müşteri ID boş olamaz
        if (customerId == null) {
            throw new IllegalArgumentException(Messages.CUSTOMER_ID_NOT_FOUND);
        }

        // Müşteriyi getir
        Customer customer = getCustomerById(customerId);

        // Müşteri varsa hayvanları getir, yoksa geçersiz argüman hatası fırlat
        if (customer.getAnimals() != null && !customer.getAnimals().isEmpty()) {
            return customer.getAnimals();
        } else {
            throw new IllegalArgumentException(Messages.NO_ANIMALS_FOUND_FOR_CUSTOMER);
        }
    }

    @Override
    public Page<Customer> cursor(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return customerRepo.findAll(pageable);
    }
}