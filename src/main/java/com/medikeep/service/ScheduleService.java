package com.medikeep.service;

import com.medikeep.domain.*;
import com.medikeep.dto.ScheduleRequestDto;
import com.medikeep.dto.ScheduleResponseDto;
import com.medikeep.repository.IntakeScheduleRepository;
import com.medikeep.repository.MedicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final IntakeScheduleRepository intakeScheduleRepository;
    private final MedicationRepository medicationRepository;

    @Transactional
    public void createSchedules(ScheduleRequestDto requestDto, User user) {
        // 1. 약 이름으로 기존 약 정보를 찾거나, 없으면 새로 생성
        Medication medication = medicationRepository.findByMedicationNameAndUser(requestDto.getMedicationName(), user)
                .orElseGet(() -> {
                    Medication newMedication = Medication.builder()
                            .medicationName(requestDto.getMedicationName())
                            .medicationType(requestDto.getMedicationType())
                            .user(user)
                            .build();
                    return medicationRepository.save(newMedication);
                });

        // 2. 선택된 요일과 시간대의 모든 조합에 대해 스케줄 생성
        for (Weekday weekday : requestDto.getWeekdays()) {
            for (TimeSlot timeSlot : requestDto.getTimeSlots()) {
                IntakeSchedule schedule = IntakeSchedule.builder()
                        .user(user)
                        .medication(medication)
                        .weekday(weekday)
                        .timeSlot(timeSlot)
                        .build();
                intakeScheduleRepository.save(schedule);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponseDto> getSchedules(User user) {
        return intakeScheduleRepository.findByUser(user).stream()
                .map(ScheduleResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteSchedule(Long scheduleId, User user) {
        IntakeSchedule schedule = intakeScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스케줄입니다."));

        // 본인의 스케줄이 맞는지 확인
        if (!schedule.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("본인의 스케줄만 삭제할 수 있습니다.");
        }

        intakeScheduleRepository.delete(schedule);
    }
}
