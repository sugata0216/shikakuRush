package com.example.shikakurush.service.user;

import com.example.shikakurush.repository.user.UserDeleteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDeleteService {

    private final UserDeleteRepository userDeleteRepository;

    @Transactional
    public void deleteUser(Integer userId) {
        userDeleteRepository.deleteScoreHistoryQuestions(userId);
        userDeleteRepository.deleteScoreHistories(userId);
        userDeleteRepository.deleteRankings(userId);
        userDeleteRepository.deleteUserTitles(userId);
        userDeleteRepository.nullifyInquiryUserId(userId);
        userDeleteRepository.nullifyReportUserId(userId);
        userDeleteRepository.deletePasswordResetTokens(userId);
        userDeleteRepository.softDeleteUser(userId);
    }
}