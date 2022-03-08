package com.nsoft.welcomebot.Controllers;


import com.nsoft.welcomebot.Entities.Trigger;
import com.nsoft.welcomebot.Services.MessageService;
import com.nsoft.welcomebot.Services.TriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/triggers")
public class TriggerController {

    private final TriggerService _triggerService;

    @Autowired
    public TriggerController(TriggerService triggerService) {
        _triggerService = triggerService;
    }

    @PostMapping
    public void createTrigger(@RequestBody Trigger trigger){
        _triggerService.createTrigger(trigger);
    }
}
