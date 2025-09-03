package ru.ed.module.bot.update;


import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@HandlerType(type = "poll")
public class Poll implements UpdateHandler {
    @Override
    public void handle(Update update) {

    }
}
