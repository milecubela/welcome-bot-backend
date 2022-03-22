package com.nsoft.welcomebot.Services;

import com.nsoft.welcomebot.Entities.Schedule;
import com.nsoft.welcomebot.Repositories.MessageRepository;
import com.nsoft.welcomebot.Repositories.ScheduleRepository;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        if (Optional.ofNullable(schedule).isEmpty()) {
            throw new IllegalArgumentException(" Scheduler has null value ");
        }
        if (Optional.ofNullable(schedule.getMessage()).isEmpty()) {
            throw new IllegalArgumentException(" Scheduler has recieved a null message entity");
        }
        boolean valid = _messageRepository.existsById(schedule.getMessage().getMessageId());
        if (!valid) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Scheduler has recieved a message entity whose ID does not exist");
        }
        schedule.setCreatedAt(LocalDate.now());
        schedule.setNextRun(schedule.getRunDate());
        _scheduleRepository.save(schedule);
    }

    public void deleteSchedule(Long scheduleId) {
        if (!_scheduleRepository.existsById(scheduleId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Schedule with the ID of : " + scheduleId + " does not exist.");
        } else {
            _scheduleRepository.deleteById(scheduleId);
        }
    }

    @Transactional
    public void updateSchedule(Schedule schedule) {
        _scheduleRepository.findById(schedule.getScheduleId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule with the ID of : " + schedule.getScheduleId() + " does not exist"));
        schedule.setNextRun(schedule.getRunDate());
        _scheduleRepository.save(schedule);
    }

    public Schedule getScheduleById(Long scheduleId) {
        return _scheduleRepository.findById(scheduleId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule with the ID of : " + scheduleId + " does not exist"));
    }

    public Page<Schedule> findAllPaginated(int offset, int pagesize) {
        return _scheduleRepository.findAll(PageRequest.of(offset, pagesize));
    }
}