package com.example.shikakurush.repository.admin;

import com.example.shikakurush.entity.Inquiry;
import com.example.shikakurush.mapper.user.InquiryMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InquiryAdminRepository {

    private final InquiryMapper inquiryMapper;

    public InquiryAdminRepository(InquiryMapper inquiryMapper) {
        this.inquiryMapper = inquiryMapper;
    }

    public List<Inquiry> findAll() {
        return inquiryMapper.findAll();
    }

    public Inquiry findById(int id) {
        return inquiryMapper.findById(id);
    }
}