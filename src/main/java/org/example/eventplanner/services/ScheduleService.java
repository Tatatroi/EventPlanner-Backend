package org.example.eventplanner.services;

import org.example.eventplanner.models.Schedule;
import org.example.eventplanner.repositories.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public Schedule getScheduleById(Long id) {
        return scheduleRepository.findById(id).orElse(null);
    }

    public Schedule createSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public Schedule updateSchedule(Long id, Schedule updatedSchedule) {
        return scheduleRepository.findById(id)
                .map(schedule -> {
                    schedule.setTitle(updatedSchedule.getTitle());
                    schedule.setDescription(updatedSchedule.getDescription());
                    schedule.setStart_time(updatedSchedule.getStart_time());
                    schedule.setEnd_time(updatedSchedule.getEnd_time());
                    schedule.setEvent(updatedSchedule.getEvent());
                    return scheduleRepository.save(schedule);
                })
                .orElseThrow(() -> new RuntimeException("Schedule not found with id " + id));
    }

    public void deleteSchedule(Long id) {
        scheduleRepository.deleteById(id);
    }
}
