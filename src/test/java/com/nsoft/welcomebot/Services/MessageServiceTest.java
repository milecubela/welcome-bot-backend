package com.nsoft.welcomebot.Services;

import com.nsoft.welcomebot.Repositories.MessageRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;
    private AutoCloseable autoCloseable;
    private MessageService messageServiceTest;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        messageServiceTest = new MessageService(messageRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void canGetAllMessages() {
        // when we call messageService.getMessages()
        messageServiceTest.getMessages();
        // Then we should verify if the mock function messageRepository.findAll() was called
        verify(messageRepository).findAll();
    }

    @Test
    @Disabled
    void getMessageById() {
    }

    @Test
    @Disabled
    void findAllPaginated() {
    }

    @Test
    @Disabled
    void createNewMessage() {
    }

    @Test
    @Disabled
    void deleteMessage() {
    }

    @Test
    @Disabled
    void updateMessage() {
    }
}