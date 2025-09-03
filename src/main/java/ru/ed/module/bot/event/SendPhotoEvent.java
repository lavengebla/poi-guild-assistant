package ru.ed.module.bot.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

@Getter
@RequiredArgsConstructor
public class SendPhotoEvent {
    private final SendPhoto sendPhoto;
}
