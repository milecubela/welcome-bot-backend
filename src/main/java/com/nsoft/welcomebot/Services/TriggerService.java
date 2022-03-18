package com.nsoft.welcomebot.Services;

import com.nsoft.welcomebot.Entities.Trigger;
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

    private final TriggerRepository _triggerRepository;
    private final MessageRepository _messageRepository;

    @Autowired
    public TriggerService(TriggerRepository triggerRepository, MessageRepository messageRepository) {
        _triggerRepository = triggerRepository;
        _messageRepository = messageRepository;
    }

    public void createTrigger(Trigger trigger) {
        if (Optional.ofNullable(trigger.getMessage()).isEmpty()) {
            throw new IllegalStateException("Trigger has recieved a null message entitiy!");
        } else if (!_messageRepository.existsById(trigger.getMessage().getMessageId())) {
            throw new IllegalStateException(" Trigger has recieved a message entity whose ID does not exist");
        }
        _triggerRepository.save(trigger);
    }

    public List<Trigger> getTriggers() {
        return _triggerRepository.findAll();
    }

    public Trigger getTriggerById(Long triggerId) {
        Trigger trigger = _triggerRepository.findById(triggerId).orElseThrow(() -> new IllegalStateException("Trigger with the ID of : " + triggerId + " does not exist"));
        return trigger;
    }

    public void deleteTrigger(Long triggerId) {
        if (!_triggerRepository.existsById(triggerId)) {
            throw new IllegalStateException("Trigger with the ID: " + triggerId + " does not exist ");
        } else {
            _triggerRepository.deleteById(triggerId);
        }
    }

    public void updateTrigger(Trigger trigger) {
        _triggerRepository.findById(trigger.getTriggerId()).orElseThrow(() -> new IllegalStateException("Trigger with the ID of : " + trigger.getTriggerId() + " does not exist"));
        _triggerRepository.save(trigger);
    }
    public Page<Trigger> findAllPaginated(int offset, int pagesize) {
        Page<Trigger> triggers = _triggerRepository.findAll(PageRequest.of(offset, pagesize));
        return triggers;
    }
}
