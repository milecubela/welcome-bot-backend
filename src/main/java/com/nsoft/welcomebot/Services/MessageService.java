package com.nsoft.welcomebot.Services;

import com.nsoft.welcomebot.Entities.Message;
import com.nsoft.welcomebot.Repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private final MessageRepository _messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        _messageRepository = messageRepository;
    }

    public List<Message> getMessages() { return _messageRepository.findAll(); }

    public void createNewMessage(Message message) {
//        var boo = message.getText().length();
//       @Valid Message message1;
//        System.out.println(boo);

        message.setCreatedAt(LocalDate.now());
        _messageRepository.save(message);
    }
    public void deleteMessage(Long messageId) {
        boolean exist = _messageRepository.existsById(messageId);
        if (!exist) {
            throw new IllegalStateException(" Message with the ID: " + messageId + " does not exist ");
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, " Message with the ID: " + messageId + " does not exist ");
        }
        _messageRepository.deleteById(messageId);

   }

    public Optional<Message> getMessageById(Long messageId) {
        boolean exist = _messageRepository.existsById(messageId);
        if (!exist) {
            throw new IllegalStateException(" Message with the ID: " + messageId + " does not exist ");
        }
        return _messageRepository.findById(messageId);
    }
}
