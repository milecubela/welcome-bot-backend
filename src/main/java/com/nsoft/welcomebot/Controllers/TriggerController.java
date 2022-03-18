package com.nsoft.welcomebot.Controllers;


import com.nsoft.welcomebot.Entities.Trigger;
import com.nsoft.welcomebot.Services.TriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    public Trigger getTriggerById(@PathVariable("triggerId") long triggerId){
        return _triggerService.getTriggerById(triggerId);
    }

    @DeleteMapping(path="{triggerId}")
    public void deleteTrigger(@PathVariable("triggerId") long triggerId){
        _triggerService.deleteTrigger(triggerId);
    }

    @PutMapping
    public void updateTrigger(@Valid @RequestBody Trigger trigger){
        _triggerService.updateTrigger(trigger);
    }

    @GetMapping("/paginated/{offset}/{pagesize}")
    public Page<Trigger> getPaginatedMessages(@PathVariable int offset, @PathVariable int pagesize){
        Page<Trigger> triggers=_triggerService.findAllPaginated(offset, pagesize);
        return triggers;
    }


}
