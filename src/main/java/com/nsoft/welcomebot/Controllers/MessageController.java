package com.nsoft.welcomebot.Controllers;

import com.nsoft.welcomebot.Entities.Message;
import com.nsoft.welcomebot.Services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/messages")
public class MessageController {

    private final MessageService _messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        _messageService = messageService;
    }

    @GetMapping
    public List<Message> getMessages() {
        return _messageService.getMessages();
    }

    @GetMapping("/paginated/{offset}/{pagesize}")
    public Page<Message> getPaginatedMessages(@PathVariable int offset,@PathVariable int pagesize){
        Page<Message> msgs=_messageService.findAllPaginated(offset, pagesize);
        return msgs;
    }


    @GetMapping(path = "{messageId}")
    public Optional<Message> getMessages(@PathVariable("messageId") long messageId) {
        return _messageService.getMessageById(messageId);
    }

    @PostMapping
    public void createMessage(@Valid @RequestBody Message message) {
        _messageService.createNewMessage(message);
    }

    @DeleteMapping(path = "{messageId}")
    public void deleteMessage(@PathVariable("messageId") long messageId) {
        _messageService.deleteMessage(messageId);
    }
}
