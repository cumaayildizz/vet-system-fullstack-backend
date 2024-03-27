package com.cuma.yildiz.VetSystem.dao;


import com.cuma.yildiz.VetSystem.entities.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepo extends JpaRepository<Report, Long> {

}