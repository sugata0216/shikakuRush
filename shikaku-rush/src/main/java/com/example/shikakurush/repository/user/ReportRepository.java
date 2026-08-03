package com.example.shikakurush.repository.user;

import com.example.shikakurush.entity.Report;
import com.example.shikakurush.mapper.user.ReportMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ReportRepository {

    private final ReportMapper reportMapper;

    public ReportRepository(ReportMapper reportMapper) {
        this.reportMapper = reportMapper;
    }

    public void save(Report report) {
        reportMapper.insert(report);
    }
}