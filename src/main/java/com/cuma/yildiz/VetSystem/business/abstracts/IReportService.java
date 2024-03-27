package com.cuma.yildiz.VetSystem.business.abstracts;

import com.cuma.yildiz.VetSystem.entities.Report;
import java.util.List;

public interface IReportService {
    Report saveReport(Report report);
    Report getReportById(Long id);
    List<Report> getAllReports();
    Report updateReport(Report report);
    void deleteReport(Long id);
}
