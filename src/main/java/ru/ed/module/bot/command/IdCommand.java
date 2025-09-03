package ru.ed.module.bot.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.ed.module.bot.event.TelegramEventPublisher;

@Component
@Exec(executable = "id")
@RequiredArgsConstructor
public class IdCommand implements Command {

    private final TelegramEventPublisher telegramEventPublisher;

    /*
    получение id
     */

    @Override
    public void execute(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setReplyToMessageId(message.getMessageId());
        StringBuilder sb = new StringBuilder();
        if (message.getChatId().toString().startsWith("-")) {
            sb
                .append("chat_id - %s %n".formatted(message.getChatId()))
                .append("thread_id - %s %n".formatted(message.getMessageThreadId()))
                .append("user_id - %s".formatted(message.getFrom().getId()));
        } else {
            sb.append("telegram_id - %s".formatted(message.getFrom().getId()));
        }
        sendMessage.setText(sb.toString());
        telegramEventPublisher.publish(sendMessage);
    }

    @Override
    public boolean requiresAdminRights() {
        return false;
    }
}
