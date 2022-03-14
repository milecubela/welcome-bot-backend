package com.nsoft.welcomebot.Controllers;


import com.nsoft.welcomebot.Entities.Trigger;
import com.nsoft.welcomebot.Services.TriggerService;
import com.nsoft.welcomebot.SlackModule.SlackModel;
import com.slack.api.methods.SlackApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/triggers")
public class TriggerController {

    private final TriggerService _triggerService;
    private final SlackModel _slackModel;


    @Autowired
    public TriggerController(TriggerService triggerService, SlackModel slackModel) {
        _triggerService = triggerService;
        _slackModel = slackModel;
    }

    @PostMapping
    public void createTrigger(@Valid @RequestBody Trigger trigger) throws SlackApiException, IOException {
        _triggerService.createTrigger(trigger);
        _slackModel.addTriggers();
    }

    @GetMapping
    public List<Trigger> getTriggers(){return _triggerService.getTriggers();}

    @GetMapping(path="{triggerId}")
    public Trigger getTriggerById(@PathVariable("triggerId") long triggerId){return _triggerService.getTriggerById(triggerId);}

    @DeleteMapping(path="{triggerId}")
    public void deleteTrigger(@PathVariable("triggerId") long triggerId) throws SlackApiException, IOException {
        _triggerService.deleteTrigger(triggerId);
        _slackModel.addTriggers();
    }

    @PutMapping
    public void updateTrigger(@Valid @RequestBody Trigger trigger) throws SlackApiException, IOException {
        _triggerService.updateTrigger(trigger);
        _slackModel.addTriggers();
    }

}
