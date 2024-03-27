package com.cuma.yildiz.VetSystem.api;

import com.cuma.yildiz.VetSystem.business.abstracts.IReportService;
import com.cuma.yildiz.VetSystem.core.config.modelMapper.IModelMapperService;
import com.cuma.yildiz.VetSystem.core.result.Result;
import com.cuma.yildiz.VetSystem.core.result.ResultData;
import com.cuma.yildiz.VetSystem.core.util.ResultHelper;
import com.cuma.yildiz.VetSystem.dto.request.Report.ReportSaveRequest;
import com.cuma.yildiz.VetSystem.dto.request.Report.ReportUpdateRequest;
import com.cuma.yildiz.VetSystem.dto.response.entitiesResponse.ReportResponse;
import com.cuma.yildiz.VetSystem.entities.Report;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/vetSystem/reports")
public class ReportController {

    private final IReportService reportService;
    private final IModelMapperService modelMapper;

    @Autowired
    public ReportController(IReportService reportService, IModelMapperService modelMapper) {
        this.reportService = reportService;
        this.modelMapper = modelMapper;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<ReportResponse> save(@Valid @RequestBody ReportSaveRequest reportSaveRequest) {
        Report saveReport = modelMapper.forRequest().map(reportSaveRequest, Report.class);
        Report savedReport = reportService.saveReport(saveReport);
        ReportResponse reportResponse = modelMapper.forResponse().map(savedReport, ReportResponse.class);
        return ResultHelper.created(reportResponse);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<ReportResponse> get(@PathVariable("id") Long id) {
        Report report = reportService.getReportById(id);
        ReportResponse reportResponse = modelMapper.forResponse().map(report, ReportResponse.class);
        return ResultHelper.success(reportResponse);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<ReportResponse>> getAllReports() {
        List<Report> reports = reportService.getAllReports();
        List<ReportResponse> reportResponses = reports.stream()
                .map(report -> modelMapper.forResponse().map(report, ReportResponse.class))
                .collect(Collectors.toList());
        return ResultHelper.success(reportResponses);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<ReportResponse> update(@PathVariable("id") Long id, @Valid @RequestBody ReportUpdateRequest reportUpdateRequest) {
        Report existingReport = reportService.getReportById(id);
        modelMapper.forRequest().map(reportUpdateRequest, existingReport);
        Report updatedReport = reportService.updateReport(existingReport);
        ReportResponse reportResponse = modelMapper.forResponse().map(updatedReport, ReportResponse.class);
        return ResultHelper.updated(reportResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable("id") Long id) {
        reportService.deleteReport(id);
        return ResultHelper.deleted();
    }
}



