package ru.ed.module.bot.update;


import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;


/*
изменения статуса людей в чатах:)
 */

@Component
@HandlerType(type = "chatMember")
public class ChatMember implements UpdateHandler {


    @Override
    public void handle(Update update) {

    }
}
