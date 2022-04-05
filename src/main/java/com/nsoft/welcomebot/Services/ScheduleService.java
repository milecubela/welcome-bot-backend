package com.nsoft.welcomebot.Services;

import com.nsoft.welcomebot.Entities.Message;
import com.nsoft.welcomebot.Entities.Schedule;
import com.nsoft.welcomebot.ExceptionHandlers.CustomExceptions.NotFoundException;
import com.nsoft.welcomebot.Models.RequestModels.ScheduleRequest;
import com.nsoft.welcomebot.Repositories.MessageRepository;
import com.nsoft.welcomebot.Repositories.ScheduleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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

    public void createNewSchedule(ScheduleRequest scheduleRequest) {
        Optional<Message> message = _messageRepository.findById(scheduleRequest.getMessageId());
        if (message.isEmpty()) {
            throw new NotFoundException("Message with ID " + scheduleRequest.getMessageId() + " not found!");
        }
        Schedule schedule = new Schedule(scheduleRequest);
        schedule.setMessage(message.get());
        schedule.setCreatedAt(LocalDate.now());
        _scheduleRepository.save(schedule);
    }

    public void deleteSchedule(Long scheduleId) {
        Optional<Schedule> optionalSchedule = _scheduleRepository.findById(scheduleId);
        if (optionalSchedule.isEmpty()) {
            throw new NotFoundException("Schedule with ID " + scheduleId + " not found");
        }
        _scheduleRepository.deleteById(scheduleId);
    }

    public Schedule updateSchedule(Long scheduleId, ScheduleRequest scheduleRequest) {
        Optional<Schedule> optionalSchedule = _scheduleRepository.findById(scheduleId);
        if (optionalSchedule.isEmpty()) {
            throw new NotFoundException("Schedule with ID " + scheduleId + " not found");
        }
        Optional<Message> optionalMessage = _messageRepository.findById(scheduleRequest.getMessageId());
        if (optionalMessage.isEmpty()) {
            throw new NotFoundException("Message with ID " + scheduleRequest.getMessageId() + " not found!");
        }
        Schedule schedule = optionalSchedule.get();
        schedule.updateSchedule(scheduleRequest);
        schedule.setMessage(optionalMessage.get());
        _scheduleRepository.save(schedule);
        return schedule;
    }

    public Schedule getScheduleById(Long scheduleId) {
        Optional<Schedule> optionalSchedule = _scheduleRepository.findById(scheduleId);
        if (optionalSchedule.isEmpty()) {
            throw new NotFoundException("Schedule with ID " + scheduleId + " not found");
        }
        return optionalSchedule.get();
    }

    public Page<Schedule> findAllPaginated(int offset, int pagesize) {
        return _scheduleRepository.findAll(PageRequest.of(offset, pagesize));
    }
}