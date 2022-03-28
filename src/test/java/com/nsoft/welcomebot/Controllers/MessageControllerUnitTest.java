package com.nsoft.welcomebot.Controllers;

import com.nsoft.welcomebot.Services.MessageService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.verify;

class MessageControllerUnitTest {

    private MessageService messageService = Mockito.mock(MessageService.class);
    private MessageController messageController = new MessageController(messageService);

    /**
     * Testing if the function will return all messages if params are not available
     * */
    @Test
    void shouldCallAllMessages() {
        //when
        messageController.getMessages(null, null);
        // then
        verify(messageService).getMessages();
    }

    /**
     * Testing if the function will return pagination if param are available
     */
    @Test
    void shouldCallFindAllPaginated() {
        //when
        messageController.getMessages(0, 2);
        // then
        verify(messageService).findAllPaginated(0, 2);
    }
}