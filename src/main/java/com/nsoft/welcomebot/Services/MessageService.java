package com.nsoft.welcomebot.Services;

import com.nsoft.welcomebot.Entities.Message;
import com.nsoft.welcomebot.Repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository _messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        _messageRepository = messageRepository;
    }

    public List<Message> getMessages() { return _messageRepository.findAll(); }

    public void createNewMessage(Message message) {
        _messageRepository.save(message);
    }
}
