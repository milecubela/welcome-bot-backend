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

    private final ScheduleRepository scheduleRepository;
    private final MessageRepository messageRepository;

    public ScheduleService(ScheduleRepository scheduleRepository, MessageRepository messageRepository) {
        this.scheduleRepository = scheduleRepository;
        this.messageRepository = messageRepository;
    }

    public List<Schedule> getSchedules() {
        return scheduleRepository.findAll();
    }

    public void createNewSchedule(ScheduleRequest scheduleRequest) {
        Optional<Message> message = messageRepository.findById(scheduleRequest.getMessageId());
        if (message.isEmpty()) {
            throw new NotFoundException("Message with ID " + scheduleRequest.getMessageId() + " not found!");
        }
        Schedule schedule = new Schedule(scheduleRequest);
        schedule.setMessage(message.get());
        schedule.setCreatedAt(LocalDate.now());
        scheduleRepository.save(schedule);
    }

    public void deleteSchedule(Long scheduleId) {
        Optional<Schedule> optionalSchedule = scheduleRepository.findById(scheduleId);
        if (optionalSchedule.isEmpty()) {
            throw new NotFoundException("Schedule with ID " + scheduleId + " not found");
        }
        scheduleRepository.deleteById(scheduleId);
    }

    public Schedule updateSchedule(Long scheduleId, ScheduleRequest scheduleRequest) {
        Optional<Schedule> optionalSchedule = scheduleRepository.findById(scheduleId);
        if (optionalSchedule.isEmpty()) {
            throw new NotFoundException("Schedule with ID " + scheduleId + " not found");
        }
        Optional<Message> optionalMessage = messageRepository.findById(scheduleRequest.getMessageId());
        if (optionalMessage.isEmpty()) {
            throw new NotFoundException("Message with ID " + scheduleRequest.getMessageId() + " not found!");
        }
        Schedule schedule = optionalSchedule.get();
        schedule.updateSchedule(scheduleRequest);
        schedule.setMessage(optionalMessage.get());
        scheduleRepository.save(schedule);
        return schedule;
    }

    public Schedule getScheduleById(Long scheduleId) {
        Optional<Schedule> optionalSchedule = scheduleRepository.findById(scheduleId);
        if (optionalSchedule.isEmpty()) {
            throw new NotFoundException("Schedule with ID " + scheduleId + " not found");
        }
        return optionalSchedule.get();
    }

    public Page<Schedule> findAllPaginated(int offset, int pagesize) {
        return scheduleRepository.findAll(PageRequest.of(offset, pagesize));
    }

    public void toggleGifs() {
        Schedule gif = scheduleRepository.getById(1L);
        if (gif.isActive()) {
            gif.setActive(false);
            scheduleRepository.save(gif);
        } else {
            gif.setActive(true);
            scheduleRepository.save(gif);
        }
    }
}