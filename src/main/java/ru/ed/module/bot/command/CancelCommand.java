package ru.ed.module.bot.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.ed.module.bot.event.TelegramEventPublisher;
import ru.ed.module.bot.service.DialogService;

@Component
@Exec(executable = "cancel")
@RequiredArgsConstructor
public class CancelCommand implements Command {

    private final TelegramEventPublisher telegramEventPublisher;
    private final DialogService dialogService;

    /*
    получение id
     */

    @Override
    public void execute(Message message) {
        dialogService.cancel(message.getFrom().getId());
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText("Режим диалога завершен, вы всегда можете вернуть к нему выполнив команду " +
            "/resume");
        telegramEventPublisher.publish(sendMessage);
    }

    @Override
    public boolean requiresAdminRights() {
        return false;
    }
}
