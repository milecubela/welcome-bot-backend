package com.nsoft.welcomebot.Controllers;


import com.nsoft.welcomebot.Entities.Trigger;
import com.nsoft.welcomebot.Services.TriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/triggers")
public class TriggerController {

    private final TriggerService _triggerService;

    @Autowired
    public TriggerController(TriggerService triggerService) {
        _triggerService = triggerService;
    }

    @PostMapping
    public void createTrigger(@Valid @RequestBody Trigger trigger){
        _triggerService.createTrigger(trigger);
    }

    @GetMapping
    public List<Trigger> getTriggers(){return _triggerService.getTriggers();}

    @GetMapping(path="{triggerId}")
    public Optional<Trigger> getTriggerById(@PathVariable("triggerId") long triggerId){
        return _triggerService.getTriggerById(triggerId);
    }
}
