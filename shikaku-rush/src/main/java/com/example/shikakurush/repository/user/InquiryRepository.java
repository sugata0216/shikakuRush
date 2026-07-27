package com.example.shikakurush.repository.user;

import com.example.shikakurush.entity.Inquiry;
import com.example.shikakurush.mapper.user.InquiryMapper;
import org.springframework.stereotype.Repository;

@Repository
public class InquiryRepository {

    private final InquiryMapper inquiryMapper;

    public InquiryRepository(InquiryMapper inquiryMapper) {
        this.inquiryMapper = inquiryMapper;
    }

    public void save(Inquiry inquiry) {
        inquiryMapper.insert(inquiry);
    }
}