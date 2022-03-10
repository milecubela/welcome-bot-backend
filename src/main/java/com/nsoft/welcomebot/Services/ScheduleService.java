package com.nsoft.welcomebot.Services;

import com.nsoft.welcomebot.Entities.Message;
import com.nsoft.welcomebot.Entities.Schedule;
import com.nsoft.welcomebot.Repositories.MessageRepository;
import com.nsoft.welcomebot.Repositories.ScheduleRepository;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {

    private final ScheduleRepository _scheduleRepository;
    private final MessageRepository _messageRepository;

    public ScheduleService(ScheduleRepository scheduleRepository, MessageRepository messageRepository) {
        _scheduleRepository = scheduleRepository;
        _messageRepository = messageRepository;
    }

    public List<Schedule> getSchedules() {
        return _scheduleRepository.findAll();
    }

    @NonNull
    public void createNewSchedule(Schedule schedule) {
        if(Optional.ofNullable(schedule).isEmpty()){
            throw new IllegalStateException(" Scheduler has null value ");
        }
        if(Optional.ofNullable(schedule.getMessage()).isEmpty()){
         throw new IllegalStateException(" Scheduler has recieved a null message entity");
        }
        boolean valid = _messageRepository.existsById(schedule.getMessage().getMessageId());
        if (!valid){
            throw new IllegalStateException(" Scheduler has recieved a message entity whose ID does not exist");
        }
        schedule.setCreated_at(LocalDate.now());
        _scheduleRepository.save(schedule);
    }

    public void deleteSchedule(Long scheduleId) {
        if (!_scheduleRepository.existsById(scheduleId)) {
            throw new IllegalStateException(" Schedule with the ID of : " + scheduleId + " does not exist.");
        } else {
            _scheduleRepository.deleteById(scheduleId);
        }
    }

    @Transactional
    public void updateSchedule(Schedule schedule) {
        Schedule sched = _scheduleRepository.findById(schedule.getScheduleId()).orElseThrow(() ->
                new IllegalStateException("Schedule with the ID of : " + schedule.getScheduleId() + " does not exist"));

        _scheduleRepository.save(schedule);
    }

    public Schedule getScheduleById(Long scheduleId) {
        Schedule sched = _scheduleRepository.findById(scheduleId).orElseThrow(() ->
                new IllegalStateException("Schedule with the ID of : " + scheduleId + " does not exist"));
        return sched;
    }
}