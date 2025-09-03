package ru.ed.module.bot.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.CopyMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Async
    public void publish(Update update) {
        log.debug("publishing new update event");
        log.trace("update: {}", update);
        UpdateReceivedEvent event = new UpdateReceivedEvent(update);
        applicationEventPublisher.publishEvent(event);
        log.debug("published new update event successfully");
    }

    @Async
    public void publish(SendMessage sendMessage) {
        log.debug("publishing new send message event");
        log.trace("send message: {}", sendMessage);
        SendMessageEvent event = new SendMessageEvent(sendMessage);
        applicationEventPublisher.publishEvent(event);
        log.debug("published new send message event successfully");
    }

    @Async
    public void publish(SendPhoto sendPhoto) {
        log.debug("publishing new send photo event");
        log.trace("send message: {}", sendPhoto);
        SendPhotoEvent event = new SendPhotoEvent(sendPhoto);
        applicationEventPublisher.publishEvent(event);
        log.debug("published new send photo event successfully");
    }

    @Async
    public void publish(CopyMessage copyMessage) {
        log.debug("publishing new copy message event");
        log.trace("copy message: {}", copyMessage);
        CopyMessageEvent event = new CopyMessageEvent(copyMessage);
        applicationEventPublisher.publishEvent(event);
        log.debug("published new copy message event successfully");
    }

    @Async
    public void publish(SendMediaGroup sendMediaGroup) {
        log.debug("publishing new send media group event");
        log.trace("send media group: {}", sendMediaGroup);
        SendMediaGroupEvent event = new SendMediaGroupEvent(sendMediaGroup);
        applicationEventPublisher.publishEvent(event);
        log.debug("published new send media group event successfully");
    }

    @Async
    public void publish(DeleteMessage deleteMessage) {
        log.debug("publishing new delete message event");
        log.trace("delete message: {}", deleteMessage);
        DeleteMessageEvent event = new DeleteMessageEvent(deleteMessage);
        applicationEventPublisher.publishEvent(event);
        log.debug("published new delete message event successfully");
    }
}
