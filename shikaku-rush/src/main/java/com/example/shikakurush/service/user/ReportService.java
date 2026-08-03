package com.example.shikakurush.service.user;

import com.example.shikakurush.entity.Report;
import com.example.shikakurush.repository.user.ReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportService {

    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Transactional
    public void report(Integer userId, Integer questionId, String detail) {
        Report report = new Report();
        report.setUserId(userId);
        report.setQuestionId(questionId);
        report.setDetail(detail);
        reportRepository.save(report);
    }
}