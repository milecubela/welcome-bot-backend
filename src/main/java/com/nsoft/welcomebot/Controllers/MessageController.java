package com.nsoft.welcomebot.Controllers;

import com.nsoft.welcomebot.Entities.Message;
import com.nsoft.welcomebot.Models.RequestModels.MessageRequest;
import com.nsoft.welcomebot.Services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<Message>> getMessages() {
        List<Message> messageList = _messageService.getMessages();
        return new ResponseEntity<>(messageList, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<Page<Message>> getPaginatedMessages(@RequestParam(name = "offset") int offset, @RequestParam(name = "pagesize") int pagesize) {
        Page<Message> messages = _messageService.findAllPaginated(offset, pagesize);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }


    @GetMapping(path = "{messageId}")
    public ResponseEntity<Optional<Message>> getMessages(@PathVariable("messageId") Long messageId) {
        Optional<Message> message = _messageService.getMessageById(messageId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createMessage(@Valid @RequestBody MessageRequest messageRequest) {
        _messageService.createNewMessage(messageRequest);
        return new ResponseEntity<>("Created new message succesfully", HttpStatus.CREATED);
    }

    @DeleteMapping(path = "{messageId}")
    public ResponseEntity<String> deleteMessage(@PathVariable("messageId") Long messageId) {
        _messageService.deleteMessage(messageId);
        return new ResponseEntity<>("Message deleted", HttpStatus.OK);
    }

    @PutMapping(path = "/{messageId}")
    public ResponseEntity<Message> updateMessage(@PathVariable("messageId") Long messageId, @Valid @RequestBody MessageRequest messageRequest) {
        Message updatedMessage = _messageService.updateMessage(messageId, messageRequest);
        return new ResponseEntity<>(updatedMessage, HttpStatus.OK);
    }
}
