package com.nsoft.welcomebot.Controllers;

import com.nsoft.welcomebot.Entities.Message;
import com.nsoft.welcomebot.Services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/messages")
public class MessageController {

    private final MessageService _messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        _messageService = messageService;
    }

    @GetMapping
    public List<Message> getMessages()  {
        return _messageService.getMessages();
    }

    @PostMapping
    public void createMessage(@RequestBody Message message) {
        _messageService.createNewMessage(message);
    }

}
