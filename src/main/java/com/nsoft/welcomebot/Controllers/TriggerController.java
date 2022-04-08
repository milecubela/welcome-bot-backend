package com.nsoft.welcomebot.Controllers;


import com.nsoft.welcomebot.Entities.Trigger;
import com.nsoft.welcomebot.Models.RequestModels.TriggerRequest;
import com.nsoft.welcomebot.Services.TriggerService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
//@CrossOrigin
@RequestMapping(path = "api/v1/triggers")
public class TriggerController {

    private final TriggerService triggerService;

    @PostMapping
    public ResponseEntity<Trigger> createTrigger(@Valid @RequestBody TriggerRequest triggerRequest) {
        Trigger returnTrigger = triggerService.createNewTrigger(triggerRequest);
        return new ResponseEntity<>(returnTrigger, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Object> getTriggers(@Valid @RequestParam(name = "offset", required = false) Integer offset, @RequestParam(name = "pagesize", required = false) Integer pagesize) {
        if (offset == null || pagesize == null) {
            List<Trigger> triggerList = triggerService.getTriggers();
            return new ResponseEntity<>(triggerList, HttpStatus.OK);
        }
        Page<Trigger> pageTriggers = triggerService.findAllPaginated(offset, pagesize);
        return new ResponseEntity<>(pageTriggers, HttpStatus.OK);
    }

    @GetMapping(path = "{triggerId}")
    public ResponseEntity<Trigger> getTriggerById(@PathVariable("triggerId") Long triggerId) {
        Trigger trigger = triggerService.getTriggerById(triggerId);
        return new ResponseEntity<>(trigger, HttpStatus.OK);
    }

    @DeleteMapping(path = "{triggerId}")
    public ResponseEntity<String> deleteTrigger(@PathVariable("triggerId") Long triggerId) {
        triggerService.deleteTrigger(triggerId);
        return new ResponseEntity<>("Trigger deleted", HttpStatus.OK);
    }

    @PutMapping(path = "/{triggerId}")
    public ResponseEntity<Trigger> updateTrigger(@PathVariable("triggerId") Long triggerId, @Valid @RequestBody TriggerRequest triggerRequest) {
        Trigger updatedTrigger = triggerService.updateTrigger(triggerId, triggerRequest);
        return new ResponseEntity<>(updatedTrigger, HttpStatus.OK);
    }
}
