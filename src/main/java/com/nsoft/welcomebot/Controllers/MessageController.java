package com.nsoft.welcomebot.Controllers;

import com.nsoft.welcomebot.Entities.Message;
import com.nsoft.welcomebot.Models.RequestModels.MessageRequest;
import com.nsoft.welcomebot.Services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
        if (message.isEmpty()) {
            return new ResponseEntity("Message with ID " + messageId + " not found!", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createMessage(@Valid @RequestBody MessageRequest messageRequest) {
        _messageService.createNewMessage(messageRequest);
        return new ResponseEntity<>("Created new message succesfully", HttpStatus.CREATED);
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
        Message updatedMessage = _messageService.updateMessage(messageId, messageRequest);
        return new ResponseEntity(updatedMessage, HttpStatus.OK);
    }

    // Response for bad @Valid requests
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity validationError(MethodArgumentNotValidException e) {
        StringBuilder string = new StringBuilder();
        BindingResult result = e.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();
        for(FieldError error : fieldErrors) string.append(error.getField() + " : " + error.getDefaultMessage() + " | ");
        return new ResponseEntity(string.toString(),HttpStatus.BAD_REQUEST);
    }
}
