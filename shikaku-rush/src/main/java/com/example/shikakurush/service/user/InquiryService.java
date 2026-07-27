package com.example.shikakurush.service.user;

import com.example.shikakurush.entity.Inquiry;
import com.example.shikakurush.repository.user.InquiryRepository;
import org.springframework.stereotype.Service;

@Service
public class InquiryService {

    private final InquiryRepository inquiryRepository;

    public InquiryService(InquiryRepository inquiryRepository) {
        this.inquiryRepository = inquiryRepository;
    }

    public void save(Integer userId, String title, String body) {
        Inquiry inquiry = new Inquiry();
        inquiry.setUserId(userId);
        inquiry.setTitle(title);
        inquiry.setBody(body);
        inquiryRepository.save(inquiry);
    }
}