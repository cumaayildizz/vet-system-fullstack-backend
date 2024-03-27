package com.cuma.yildiz.VetSystem.business.rules;

import com.cuma.yildiz.VetSystem.core.config.exceptions.NotFoundException;
import com.cuma.yildiz.VetSystem.dao.DoctorRepo;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service//IOC icine yerlessin diye Service anotasyonu ekledik
public class DoctorBusinessRules {
    private DoctorRepo doctorRepo;

    //Dependecy Injection yapacagimiz icin metodlari static olusturmamiza gerek yok
    public void checkIfDoctorExists(String name , String mail){
       /* if (this.doctorRepo.existByName(name) || this.doctorRepo.existByMail(mail)){
            throw new NotFoundException(" Eksik ve ya hatali veri girisi");
        }*/
    }
}
