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
        try {
            List<Message> messageList = _messageService.getMessages();
            return new ResponseEntity<>(messageList, HttpStatus.FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{offset}/{pagesize}")
    public ResponseEntity<Page<Message>> getPaginatedMessages(@PathVariable int offset, @PathVariable int pagesize) {
        Page<Message> messages = _messageService.findAllPaginated(offset, pagesize);
        if (messages.isEmpty()) {
            return new ResponseEntity("No items on that page", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(messages, HttpStatus.FOUND);
    }


    @GetMapping(path = "{messageId}")
    public ResponseEntity<Optional<Message>> getMessages(@PathVariable("messageId") Long messageId) {
        Optional<Message> message = _messageService.getMessageById(messageId);
        if (message.isEmpty()) {
            return new ResponseEntity("Message with ID " + messageId + " not found!", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.FOUND);
    }

    @PostMapping
    public ResponseEntity<String> createMessage(@Valid @RequestBody MessageRequest messageRequest) {
        try {
            _messageService.createNewMessage(messageRequest);
            return new ResponseEntity<>("Created new message succesfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Bad request!", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(path = "{messageId}")
    public ResponseEntity<String> deleteMessage(@PathVariable("messageId") Long messageId) {
        Optional<Message> message = _messageService.getMessageById(messageId);
        if (message.isEmpty()) {
            return new ResponseEntity<>("Message with ID " + messageId + " not found!", HttpStatus.NOT_FOUND);
        }
        _messageService.deleteMessage(messageId);
        return new ResponseEntity<>("Message deleted", HttpStatus.OK);
    }

    @PutMapping(path = "/{messageId}")
    public ResponseEntity<Message> updateMessage(@PathVariable("messageId") Long messageId, @Valid @RequestBody MessageRequest messageRequest) {
        Optional<Message> message = _messageService.getMessageById(messageId);
        if (message.isEmpty()) {
            return new ResponseEntity("Message with id " + messageId + " not found!", HttpStatus.NOT_FOUND);
        }
        try{
            Message updatedMessage = _messageService.updateMessage(messageId, messageRequest);
            return new ResponseEntity(updatedMessage, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity("Bad request!", HttpStatus.BAD_REQUEST);
        }

    }
}
