package com.example.shikakurush.service.admin;

import com.example.shikakurush.entity.Inquiry;
import com.example.shikakurush.repository.admin.InquiryAdminRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InquiryAdminService {

    private final InquiryAdminRepository inquiryAdminRepository;

    public InquiryAdminService(InquiryAdminRepository inquiryAdminRepository) {
        this.inquiryAdminRepository = inquiryAdminRepository;
    }

    public List<Inquiry> findAll() {
        return inquiryAdminRepository.findAll();
    }

    public Inquiry findById(int id) {
        return inquiryAdminRepository.findById(id);
    }
}