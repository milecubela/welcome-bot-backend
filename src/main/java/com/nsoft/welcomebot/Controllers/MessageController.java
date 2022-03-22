package com.nsoft.welcomebot.Controllers;

import com.nsoft.welcomebot.Entities.Message;
import com.nsoft.welcomebot.Models.RequestModels.MessageRequest;
import com.nsoft.welcomebot.Services.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "api/v1/messages")
public class MessageController {

    private final MessageService _messageService;

    @GetMapping
    public ResponseEntity<Object> getMessages(@Valid @RequestParam(name = "offset", required = false) Integer offset, @RequestParam(name = "pagesize", required = false) Integer pagesize) {
        if (offset == null || pagesize == null) {
            List<Message> messageList = _messageService.getMessages();
            return new ResponseEntity<>(messageList, HttpStatus.OK);
        }
        Page<Message> pageMessages = _messageService.findAllPaginated(offset, pagesize);
        return new ResponseEntity<>(pageMessages, HttpStatus.OK);
    }

    @GetMapping(path = "{messageId}")
    public ResponseEntity<Message> getMessages(@PathVariable("messageId") Long messageId) {
        Message message = _messageService.getMessageById(messageId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createMessage(@Valid @RequestBody MessageRequest messageRequest) {
        _messageService.createNewMessage(messageRequest);
        return new ResponseEntity<>("Created new message successfully", HttpStatus.CREATED);
    }

    @DeleteMapping(path = "{messageId}")
    public ResponseEntity<String> deleteMessage(@PathVariable("messageId") Long messageId) {
        _messageService.deleteMessage(messageId);
        return new ResponseEntity<>("Message deleted", HttpStatus.OK);
    }

    @PutMapping(path = "/{messageId}")
    public ResponseEntity<Message> updateMessage(@PathVariable("messageId") Long messageId, @Valid @RequestBody MessageRequest messageRequest) {
        Message updatedMessage = _messageService.updateMessage(messageId, messageRequest);
        return new ResponseEntity(updatedMessage, HttpStatus.OK);
    }
}
