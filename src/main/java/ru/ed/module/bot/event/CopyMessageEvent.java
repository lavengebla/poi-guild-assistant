package ru.ed.module.bot.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.CopyMessage;

@Getter
@RequiredArgsConstructor
public class CopyMessageEvent {
    private final CopyMessage copyMessage;
}
