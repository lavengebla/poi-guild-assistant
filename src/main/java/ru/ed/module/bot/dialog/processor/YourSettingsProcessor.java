package ru.ed.module.bot.dialog.processor;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ForceReplyKeyboard;
import ru.ed.module.bot.event.TelegramEventPublisher;
import ru.ed.module.bot.model.DialogEntity;
import ru.ed.module.bot.service.ImageService;

@Component
@DialogPartProcessor("your_settings")
public class YourSettingsProcessor extends AbstractProcessor {

    public YourSettingsProcessor(ImageService imageService, TelegramEventPublisher telegramEventPublisher) {
        super(imageService, telegramEventPublisher);
    }

    @Override
    public void proceedQuestion(DialogEntity entity) {
        SendPhoto ingameSettings = new SendPhoto();
        ingameSettings.setChatId(entity.getDialogSession().getTelegramId());

        InputFile inputFile = new InputFile();
        inputFile.setMedia(imageService.getImage("mock.jpg"), "mock.jpg");
        ingameSettings.setPhoto(inputFile);
        ingameSettings.setParseMode("html");
        ingameSettings.setCaption("""
                ⚪️Скрин «Настройки»⚪️⚪️⚪️⚪️
                Сделай скриншот своего экрана настроек и отправь его мне в следующем сообщении.
                """);

        ForceReplyKeyboard forceReplyKeyboard = new ForceReplyKeyboard();
        ingameSettings.setReplyMarkup(forceReplyKeyboard);

        telegramEventPublisher.publish(ingameSettings);
    }

    @Override
    public void handleResponse(Update update) {

    }
}
