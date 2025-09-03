package ru.ed.module.bot.update;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateHandler {

    void handle(Update update);

}
