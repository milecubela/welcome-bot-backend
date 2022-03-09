package com.nsoft.welcomebot.Services;

import com.nsoft.welcomebot.Entities.Message;
import com.nsoft.welcomebot.Entities.Schedule;
import com.nsoft.welcomebot.Repositories.MessageRepository;
import com.nsoft.welcomebot.Repositories.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.util.List;

@Service
public class ScheduleService {

    private final ScheduleRepository _scheduleRepository;
    private final MessageRepository _messageRepository;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository,MessageRepository messageRepository) {
        _scheduleRepository = scheduleRepository;
        _messageRepository = messageRepository;
    }

    public List<Schedule> getSchedules() {
        return _scheduleRepository.findAll();
    }

//      ERROR : : detached entity passed to persist
    public void createNewSchedule(Schedule schedule) {
        schedule.setCreated_at(LocalDate.now());
        if (schedule.getMessage() == null){
            throw new IllegalStateException(" Scheduler has recieved a null message entity");
        }else{
            var msg=  _messageRepository.findById(schedule.getMessage().getMessageId());

           _scheduleRepository.save(schedule);
        }
    }


    public void deleteSchedule(Long scheduleId) {


    }

//    public Optional<Schedule> getScheduleById(Long scheduleId) {
//
//    }
}