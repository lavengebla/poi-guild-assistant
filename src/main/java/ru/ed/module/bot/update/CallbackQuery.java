package ru.ed.module.bot.update;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ForceReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ru.ed.module.bot.event.TelegramEventPublisher;
import ru.ed.module.bot.model.DialogEntity;
import ru.ed.module.bot.model.DialogSession;
import ru.ed.module.bot.model.DialogType;
import ru.ed.module.bot.service.DialogService;
import ru.ed.module.bot.service.ImageService;

import java.util.ArrayList;
import java.util.List;

@Component
@HandlerType(type = "callbackQuery")
@RequiredArgsConstructor
public class CallbackQuery implements UpdateHandler {

    private final TelegramEventPublisher telegramEventPublisher;
    private final DialogService dialogService;
    private final ImageService imageService;

    @Override
    public void handle(Update update) {
        if (update.getCallbackQuery().getData().equals("/rules_accept")) {
            createRegDialog(update);
        }
    }

    private void createRegDialog(Update update) {
        DialogSession dialogSession = new DialogSession();
        dialogSession.setDialogType(DialogType.REGISTRATION);
        dialogSession.setTelegramId(update.getCallbackQuery().getFrom().getId());
        dialogSession.setActive(true);

        List<DialogEntity> dialogEntities = new ArrayList<>();

        DialogEntity nameDialog = new DialogEntity();
        nameDialog.setProcessor("your_name");
        nameDialog.setOrder(1);
        dialogEntities.add(nameDialog);

        DialogEntity settings = new DialogEntity();
        settings.setProcessor("your_settings");
        settings.setOrder(2);
        dialogEntities.add(settings);

        DialogEntity nickName = new DialogEntity();
        nickName.setProcessor("your_nickname");
        nickName.setOrder(3);
        dialogEntities.add(nickName);

        dialogService.create(dialogSession, dialogEntities);
    }
}
