package com.nsoft.welcomebot.Services;

import com.nsoft.welcomebot.Entities.Trigger;
import com.nsoft.welcomebot.Repositories.TriggerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
