package com.medikeep.service;

import com.medikeep.domain.EnvLog;
import com.medikeep.domain.User;
import com.medikeep.dto.EnvLogResponseDto;
import com.medikeep.dto.EnvUploadRequestDto;
import com.medikeep.repository.EnvLogRepository;
import com.medikeep.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class EnvLogService {
    private final EnvLogRepository envLogRepository;
    private final UserRepository userRepository;

    public EnvLog upload(User user, EnvUploadRequestDto req) {

        EnvLog log = EnvLog.builder()
                .user(user)
                .temperature(req.getTemperature())
                .humidity(req.getHumidity())
                .magnet(req.getMagnet())
                .build();

        return envLogRepository.save(log);
    }

    @Transactional(readOnly = true)
    public Optional<EnvLog> getLatest(User user) {
        return envLogRepository.findTopByUserOrderByCreatedAtDesc(user);
    }

    @Transactional(readOnly = true)
    public List<EnvLogResponseDto> getDaily(User user, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end   = date.plusDays(1).atStartOfDay().minusNanos(1);
        return envLogRepository
                .findAllByUserAndCreatedAtBetweenOrderByCreatedAtDesc(user, start, end)
                .stream()
                .map(EnvLogResponseDto::from)
                .toList();
    }
}