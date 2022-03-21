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

@Service
public class MessageService {

    private final MessageRepository _messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        _messageRepository = messageRepository;
    }

    public List<Message> getMessages() {
        return _messageRepository.findAll();
    }

    public Message getMessageById(Long messageId) {
        if (_messageRepository.findById(messageId).isEmpty())
            throw new NotFoundException("Message with ID " + messageId + " not found!");
        return _messageRepository.getById(messageId);
    }

    public Page<Message> findAllPaginated(int offset, int pagesize) {
        return _messageRepository.findAll(PageRequest.of(offset, pagesize));
    }

    public void createNewMessage(MessageRequest messageRequest) {
        Message message = new Message(messageRequest);
        message.setCreatedAt(LocalDate.now());
        _messageRepository.save(message);
    }

    public void deleteMessage(Long messageId) {
        if (_messageRepository.findById(messageId).isEmpty())
            throw new NotFoundException("Message with ID " + messageId + " not found!");
        _messageRepository.deleteById(messageId);
    }

    public Message updateMessage(Long messageId, MessageRequest messageRequest) {
        if (_messageRepository.findById(messageId).isEmpty())
            throw new NotFoundException("Message with ID " + messageId + " not found");
        Message message = _messageRepository.getById(messageId);
        message.setText(messageRequest.getText());
        message.setTitle(messageRequest.getTitle());
        _messageRepository.save(message);
        return message;
    }
}
