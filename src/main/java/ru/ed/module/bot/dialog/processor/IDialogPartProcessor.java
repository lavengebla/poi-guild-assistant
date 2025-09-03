package ru.ed.module.bot.dialog.processor;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.ed.module.bot.model.DialogEntity;

public interface IDialogPartProcessor {

    void proceedQuestion(DialogEntity entity);

    void handleResponse(Update update);

}
