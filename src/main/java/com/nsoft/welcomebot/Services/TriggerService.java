package com.nsoft.welcomebot.Services;

import com.nsoft.welcomebot.Entities.Trigger;
import com.nsoft.welcomebot.Repositories.TriggerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class TriggerService {
    private final TriggerRepository _triggerRepository;

    @Autowired
    public TriggerService(TriggerRepository triggerRepository) {
        _triggerRepository = triggerRepository;
    }

    public void createTrigger(Trigger trigger){
        _triggerRepository.save(trigger);
    }

    public List<Trigger> getTriggers(){return _triggerRepository.findAll(); }

    public Optional<Trigger> getTriggerById(Long triggerId){
        boolean exist = _triggerRepository.existsById(triggerId);
        if (!exist) {
            throw new IllegalStateException(" Trigger with the ID: " + triggerId + " does not exist ");
        }
        return _triggerRepository.findById(triggerId);
    }
}
