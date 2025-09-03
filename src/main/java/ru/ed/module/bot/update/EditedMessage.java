package ru.ed.module.bot.update;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@HandlerType(type = "editedMessage")
public class EditedMessage implements UpdateHandler{
    @Override
    public void handle(Update update) {
        log.info("123");
    }
}
