package com.medikeep.service;

import com.medikeep.domain.DeviceStatus;
import com.medikeep.domain.User;
import com.medikeep.dto.DeviceStatusUpdateRequestDto;
import com.medikeep.repository.DeviceStatusRepository;
import com.medikeep.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceStatusRepository deviceStatusRepository;
    private final UserRepository userRepository;

    @Transactional
    public void updateDeviceStatus(DeviceStatusUpdateRequestDto requestDto) {
        // 1. DTO에서 받은 userId로 사용자 조회
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다. ID: " + requestDto.getUserId()));

        // 2. 해당 사용자의 장치 상태 정보 조회
        DeviceStatus deviceStatus = deviceStatusRepository.findByUser(user)
                .orElse(null); // 없으면 null

        if (deviceStatus != null) {
            // 3a. 기존 상태 정보가 있으면 업데이트
            deviceStatus.updateStatus(
                    requestDto.getTemperature(),
                    requestDto.getHumidity(),
                    requestDto.getHasMedication()
            );
        } else {
            // 3b. 기존 상태 정보가 없으면 새로 생성하여 저장
            DeviceStatus newDeviceStatus = DeviceStatus.builder()
                    .user(user)
                    .temperature(requestDto.getTemperature())
                    .humidity(requestDto.getHumidity())
                    .hasMedication(requestDto.getHasMedication())
                    .build();
            deviceStatusRepository.save(newDeviceStatus);
        }
    }
}
