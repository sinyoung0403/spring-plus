package org.example.expert.domain.log.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.log.entity.ActionLog;
import org.example.expert.domain.log.repository.LogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoggingService {

    private final LogRepository logRepository;

    /**
     * 로그 저장
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveLog(LocalDateTime timestamp, String actionMethod, String url, Boolean success, Long userId, String errorMessage) {
        // 1. Log 생성
        ActionLog Actionlog = ActionLog.builder()
                .timestamp(timestamp)
                .actionMethod(actionMethod)
                .request_uri(url)
                .success(success)
                .initiatorId(userId)
                .errorMessage(errorMessage)
                .build();
        // 2. 저장
        logRepository.save(Actionlog);
    }
}
