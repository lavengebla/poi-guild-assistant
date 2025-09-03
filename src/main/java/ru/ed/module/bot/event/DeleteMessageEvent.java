package ru.ed.module.bot.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;

@Getter
@RequiredArgsConstructor
public class DeleteMessageEvent {
    private final DeleteMessage deleteMessage;
}
