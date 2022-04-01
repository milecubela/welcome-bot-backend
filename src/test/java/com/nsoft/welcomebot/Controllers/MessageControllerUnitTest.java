package com.nsoft.welcomebot.Controllers;

import com.nsoft.welcomebot.Services.MessageService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.verify;

class MessageControllerUnitTest {

    private final MessageService messageService = Mockito.mock(MessageService.class);
    private final MessageController messageController = new MessageController(messageService);

    /**
     * Testing if the function will return all messages if params are not available
     */
    @Test
    void canCallAllMessages() {
        //when
        messageController.getMessages(null, null);
        // then
        verify(messageService).getMessages();
    }

    /**
     * Testing if the function will return pagination if param are available
     */
    @Test
    void canCallFindAllPaginated() {
        //when
        messageController.getMessages(0, 2);
        // then
        verify(messageService).findAllPaginated(0, 2);
    }
}