package com.nsoft.welcomebot.Services;

import com.nsoft.welcomebot.Entities.Message;
import com.nsoft.welcomebot.Entities.Trigger;
import com.nsoft.welcomebot.ExceptionHandlers.CustomExceptions.NotFoundException;
import com.nsoft.welcomebot.Models.RequestModels.TriggerRequest;
import com.nsoft.welcomebot.Repositories.MessageRepository;
import com.nsoft.welcomebot.Repositories.TriggerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TriggerService {

    private final TriggerRepository triggerRepository;
    private final MessageRepository messageRepository;

    @Autowired
    public TriggerService(TriggerRepository triggerRepository, MessageRepository messageRepository) {
        this.triggerRepository = triggerRepository;
        this.messageRepository = messageRepository;
    }

    public Trigger createNewTrigger(TriggerRequest triggerRequest) {
        Optional<Message> optionalMessage = messageRepository.findById(triggerRequest.getMessageId());
        if (optionalMessage.isEmpty()) {
            throw new NotFoundException("Message with ID " + triggerRequest.getMessageId() + " not found!");
        }
        Trigger trigger = new Trigger(triggerRequest);
        trigger.setMessage(optionalMessage.get());
        return triggerRepository.save(trigger);
    }

    public List<Trigger> getTriggers() {
        return triggerRepository.findAll();
    }

    public Trigger getTriggerById(Long triggerId) {
        Optional<Trigger> optionalTrigger = triggerRepository.findById(triggerId);
        if (optionalTrigger.isEmpty()) {
            throw new NotFoundException("Trigger with ID " + triggerId + " not found!");
        }
        return optionalTrigger.get();
    }

    public void deleteTrigger(Long triggerId) {
        Optional<Trigger> optionalTrigger = triggerRepository.findById(triggerId);
        if (optionalTrigger.isEmpty()) {
            throw new NotFoundException("Trigger with ID " + triggerId + " not found!");
        }
        triggerRepository.deleteById(triggerId);
    }

    public Trigger updateTrigger(Long triggerId, TriggerRequest triggerRequest) {
        Optional<Trigger> optionalTrigger = triggerRepository.findById(triggerId);
        Optional<Message> optionalMessage = messageRepository.findById(triggerRequest.getMessageId());
        if (optionalTrigger.isEmpty()) {
            throw new NotFoundException("Trigger with ID " + triggerId + " not found!");
        } else if (optionalMessage.isEmpty()) {
            throw new NotFoundException("Message with ID " + triggerRequest.getMessageId() + " not found!");
        }
        Trigger trigger = optionalTrigger.get();
        trigger.setTriggerEvent(triggerRequest.getTriggerEvent());
        trigger.setChannel(triggerRequest.getChannel());
        trigger.setIsActive(triggerRequest.getIsActive());
        trigger.setMessage(optionalMessage.get());
        triggerRepository.save(trigger);
        return trigger;
    }

    public Page<Trigger> findAllPaginated(int offset, int pagesize) {
        return triggerRepository.findAll(PageRequest.of(offset, pagesize));
    }
}
