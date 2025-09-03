package ru.ed.module.bot.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Getter
@RequiredArgsConstructor
public class SendMediaGroupEvent {
    private final SendMediaGroup sendMediaGroup;
}
