package ru.ed.module.bot.dialog.processor;

import lombok.RequiredArgsConstructor;
import ru.ed.module.bot.event.TelegramEventPublisher;
import ru.ed.module.bot.service.ImageService;

@RequiredArgsConstructor
public abstract class AbstractProcessor implements IDialogPartProcessor {

    protected final ImageService imageService;
    protected final TelegramEventPublisher telegramEventPublisher;


}
