package ru.ed.module.bot.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

@Getter
@RequiredArgsConstructor
public class UpdateReceivedEvent {
    private final Update update;
}
