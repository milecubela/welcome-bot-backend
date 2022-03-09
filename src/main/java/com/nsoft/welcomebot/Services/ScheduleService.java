package com.nsoft.welcomebot.Services;

import com.nsoft.welcomebot.Entities.Schedule;
import com.nsoft.welcomebot.Repositories.MessageRepository;
import com.nsoft.welcomebot.Repositories.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class ScheduleService {

    private final ScheduleRepository _scheduleRepository;
    private final MessageRepository _messageRepository;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository, MessageRepository messageRepository) {
        _scheduleRepository = scheduleRepository;
        _messageRepository = messageRepository;
    }

    public List<Schedule> getSchedules() {
        return _scheduleRepository.findAll();
    }

    public void createNewSchedule(Schedule schedule) {
        boolean valid = _messageRepository.existsById(schedule.getMessage().getMessageId());
        if (!valid){
            throw new IllegalStateException(" Scheduler has recieved a message entity whose ID does not exist");
        }
        schedule.setCreated_at(LocalDate.now());
        if (schedule.getMessage() == null ) {
            throw new IllegalStateException(" Scheduler has recieved a null message entity or the message ID does not exist");
        } else {
            _scheduleRepository.save(schedule);
        }
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