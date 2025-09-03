package ru.ed.module.bot.update;


import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@HandlerType(type = "chatJoinRequest")
public class ChatJoinRequest implements UpdateHandler{
    @Override
    public void handle(Update update) {

    }
}
