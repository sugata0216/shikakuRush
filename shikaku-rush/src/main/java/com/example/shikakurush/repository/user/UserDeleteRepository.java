package com.example.shikakurush.repository.user;

import com.example.shikakurush.mapper.user.UserDeleteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserDeleteRepository {

    private final UserDeleteMapper userDeleteMapper;

    public void deleteScoreHistoryQuestions(Integer userId) {
        userDeleteMapper.deleteScoreHistoryQuestions(userId);
    }

    public void deleteScoreHistories(Integer userId) {
        userDeleteMapper.deleteScoreHistories(userId);
    }

    public void deleteRankings(Integer userId) {
        userDeleteMapper.deleteRankings(userId);
    }

    public void deleteUserTitles(Integer userId) {
        userDeleteMapper.deleteUserTitles(userId);
    }

    public void nullifyInquiryUserId(Integer userId) {
        userDeleteMapper.nullifyInquiryUserId(userId);
    }

    public void nullifyReportUserId(Integer userId) {
        userDeleteMapper.nullifyReportUserId(userId);
    }

    public void deletePasswordResetTokens(Integer userId) {
        userDeleteMapper.deletePasswordResetTokens(userId);
    }

    public void softDeleteUser(Integer userId) {
        userDeleteMapper.softDeleteUser(userId);
    }
}