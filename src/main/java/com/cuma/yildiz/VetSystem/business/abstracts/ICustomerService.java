package com.cuma.yildiz.VetSystem.business.abstracts;

import com.cuma.yildiz.VetSystem.entities.Animal;
import com.cuma.yildiz.VetSystem.entities.Customer;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ICustomerService {

    // Müşteri kaydetme işlemi
    Customer saveCustomer(Customer customer);

    // Belirli bir müşteriyi id'ye göre getirme
    Customer getCustomerById(Long id);

    // Tüm müşterileri getirme
    List<Customer> getAllCustomers();

    // Müşteri güncelleme işlemi
    Customer updateCustomer(Customer customer);

    // Belirli bir müşteriyi silme
    void deleteCustomer(Long id);

    // Müşteri adına göre müşteri arama
    List<Customer> getCustomersByName(String name);

    // Müşteriye ait tüm hayvanları getirme
    List<Animal> getAnimalsByCustomerId(Long customerId);

    // Sayfalı bir şekilde müşteri listesi getirme
    Page<Customer> cursor(int page , int pageSize);

}