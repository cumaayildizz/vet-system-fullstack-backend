package com.cuma.yildiz.VetSystem.business.concretes;

import com.cuma.yildiz.VetSystem.business.abstracts.IReportService;
import com.cuma.yildiz.VetSystem.core.config.exceptions.NotFoundException;
import com.cuma.yildiz.VetSystem.core.util.Messages;
import com.cuma.yildiz.VetSystem.dao.ReportRepo;
import com.cuma.yildiz.VetSystem.entities.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportManager implements IReportService {

    private final ReportRepo reportRepository;

    @Autowired
    public ReportManager(ReportRepo reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Override
    public Report saveReport(Report report) {

        return reportRepository.save(report);
    }

    @Override
    public Report updateReport(Report report) {

        return reportRepository.save(report);
    }

    @Override
    public Report getReportById(Long id) {

        return reportRepository.findById(id).orElseThrow(() -> new NotFoundException(Messages.NOT_FOUND));
    }

    @Override
    public List<Report> getAllReports() {

        return reportRepository.findAll();
    }

    @Override
    public void deleteReport(Long id) {

        reportRepository.deleteById(id);
    }

}
