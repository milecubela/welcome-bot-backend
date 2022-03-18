package com.nsoft.welcomebot.Services;

import com.nsoft.welcomebot.Entities.Message;
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

    private final MessageRepository _messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        _messageRepository = messageRepository;
    }

    public List<Message> getMessages() {
        return _messageRepository.findAll();
    }

    public void createNewMessage(MessageRequest messageRequest) {
        Message message = new Message(messageRequest);
        message.setCreatedAt(LocalDate.now());
        _messageRepository.save(message);
    }

    public void deleteMessage(Long messageId) {
        _messageRepository.deleteById(messageId);
    }

    public Optional<Message> getMessageById(Long messageId) {
        return _messageRepository.findById(messageId);
    }

    public Page<Message> findAllPaginated(int offset,int pagesize){
        Page<Message> messages = _messageRepository.findAll(PageRequest.of(offset, pagesize));
        return messages;
    }

    public Message updateMessage(Long messageId, MessageRequest messageRequest) {
        Message message = _messageRepository.getById(messageId);
        message.setText(messageRequest.getText());
        message.setTitle(messageRequest.getTitle());
        _messageRepository.save(message);
        return message;
    }
}
