package ru.ed.module.bot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatAdministrators;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.ed.module.bot.configuration.BotSettings;
import ru.ed.module.bot.event.CopyMessageEvent;
import ru.ed.module.bot.event.DeleteMessageEvent;
import ru.ed.module.bot.event.SendMediaGroupEvent;
import ru.ed.module.bot.event.SendMessageEvent;
import ru.ed.module.bot.event.SendPhotoEvent;
import ru.ed.module.bot.event.TelegramEventPublisher;

import java.util.List;

@Slf4j
public class TelegramBotImpl extends TelegramLongPollingBot {

    private final BotSettings botSettings;

    @Autowired
    private TelegramEventPublisher telegramEventPublisher;

    public TelegramBotImpl(BotSettings botSettings) {
        super(new DefaultBotOptions(), botSettings.getToken());
        this.botSettings = botSettings;
    }

    public TelegramBotImpl(DefaultBotOptions botOptions,
                           BotSettings botSettings) {
        super(botOptions, botSettings.getToken());
        this.botSettings = botSettings;
    }


    @Override
    public String getBotUsername() {
        return botSettings.getName();
    }

    @Override
    public void onUpdateReceived(Update update) {
        telegramEventPublisher.publish(update);
    }

    public Chat getChatInfo(String id) throws TelegramApiException {
        GetChat getChat = new GetChat();
        getChat.setChatId(id);
        return execute(getChat);
    }

    public ChatMember getChatMember(String id, Long userId) throws TelegramApiException {
        GetChatMember chatMember = new GetChatMember();
        chatMember.setChatId(id);
        chatMember.setUserId(userId);
        return execute(chatMember);
    }

    public List<ChatMember> getChatAdministrator(Long id) throws TelegramApiException {
        GetChatAdministrators getChatAdministrators = new GetChatAdministrators();
        getChatAdministrators.setChatId(id);
        return execute(getChatAdministrators);
    }

    public java.io.File getFile(String id) throws TelegramApiException {
        GetFile getFile = new GetFile();
        getFile.setFileId(id);
        File file = execute(getFile);
        return downloadFile(file);
    }

    @EventListener
    private void sendMessage(SendMessageEvent sendMessageEvent) {
        try {
            execute(sendMessageEvent.getSendMessage());
        } catch (TelegramApiException e) {
            log.error("failed to send message: {}", sendMessageEvent.getSendMessage(), e);
        }
    }

    @EventListener
    private void sendPhoto(SendPhotoEvent sendPhotoEvent) {
        try {
            execute(sendPhotoEvent.getSendPhoto());
        } catch (TelegramApiException e) {
            log.error("failed to send photo: {}", sendPhotoEvent.getSendPhoto(), e);
        }
    }

    @EventListener
    private void copyMessage(CopyMessageEvent copyMessageEvent) {
        try {
            execute(copyMessageEvent.getCopyMessage());
        } catch (TelegramApiException e) {
            log.error("failed to send copy message: {}", copyMessageEvent.getCopyMessage(), e);
        }
    }

    @EventListener
    private void sendMediaGroup(SendMediaGroupEvent sendMediaGroupEvent) {
        try {
            execute(sendMediaGroupEvent.getSendMediaGroup());
        } catch (TelegramApiException e) {
            log.error("failed to send media group: {}", sendMediaGroupEvent.getSendMediaGroup(), e);
        }
    }

    @EventListener
    private void deleteMessage(DeleteMessageEvent deleteMessageEvent) {
        try {
            execute(deleteMessageEvent.getDeleteMessage());
        } catch (TelegramApiException e) {
            log.error("failed to delete: {}", deleteMessageEvent.getDeleteMessage(), e);
        }
    }
}
