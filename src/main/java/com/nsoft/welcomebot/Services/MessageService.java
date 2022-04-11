package com.nsoft.welcomebot.Services;

import com.nsoft.welcomebot.Entities.Message;
import com.nsoft.welcomebot.ExceptionHandlers.CustomExceptions.NotFoundException;
import com.nsoft.welcomebot.Models.RequestModels.MessageRequest;
import com.nsoft.welcomebot.Repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> getMessages() {
        return messageRepository.findAll();
    }

    public Message getMessageById(Long messageId) {
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if (optionalMessage.isEmpty()) {
            throw new NotFoundException("Message with ID " + messageId + " not found!");
        }
        return optionalMessage.get();
    }

    public Page<Message> findAllPaginated(int offset, int pagesize) {
        return messageRepository.findAll(PageRequest.of(offset, pagesize));
    }

    public Message createNewMessage(MessageRequest messageRequest) {
        Message message = new Message(messageRequest);
        message.setCreatedAt(LocalDate.now());
        return messageRepository.save(message);
    }

    public void deleteMessage(Long messageId) {
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if (optionalMessage.isEmpty()) {
            throw new NotFoundException("Message with ID " + messageId + " not found!");
        }
        messageRepository.deleteById(messageId);
    }

    public Message updateMessage(Long messageId, MessageRequest messageRequest) {
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if (optionalMessage.isEmpty())
            throw new NotFoundException("Message with ID " + messageId + " not found!");
        Message message = optionalMessage.get();
        message.setText(messageRequest.getText());
        message.setTitle(messageRequest.getTitle());
        messageRepository.save(message);
        return message;
    }
}
