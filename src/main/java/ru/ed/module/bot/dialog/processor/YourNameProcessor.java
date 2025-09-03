package ru.ed.module.bot.dialog.processor;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ForceReplyKeyboard;
import ru.ed.module.back.service.TempUserService;
import ru.ed.module.bot.event.TelegramEventPublisher;
import ru.ed.module.bot.model.DialogEntity;
import ru.ed.module.bot.service.ImageService;

@Component
@DialogPartProcessor("your_name")
public class YourNameProcessor extends DialogProcessor {

    public YourNameProcessor(ImageService imageService,
                             TelegramEventPublisher telegramEventPublisher,
                             TempUserService tempUserService) {
        super(imageService, telegramEventPublisher, tempUserService);
    }

    @Override
    public void proceedQuestion(DialogEntity entity) {
        SendPhoto yourName = new SendPhoto();
        yourName.setChatId(entity.getDialogSession().getTelegramId());

        InputFile inputFile = new InputFile();
        inputFile.setMedia(imageService.getImage("mock"), "mock");
        yourName.setPhoto(inputFile);
        yourName.setParseMode("html");
        yourName.setCaption("""
                Твое настоящее Имя ⚪️⚪️⚪️⚪️⚪️⚪️
                Ты уже знаешь, что меня зовут AURI BOT🥰
                Но я не знаю, какое настоящее имя у тебя:(
                
                Пожалуйста, напиши свое настоящее имя с заглавной буквы, чтобы я правильно к тебе обращался и правильно занес твою карточку в Базу клана.
                """);
        ForceReplyKeyboard forceReplyKeyboard = new ForceReplyKeyboard();
        yourName.setReplyMarkup(forceReplyKeyboard);

        telegramEventPublisher.publish(yourName);
    }

    @Override
    public void handleResponse(Update update) {
        tempUserService.addUserResponse(update.getMessage().getChatId(), "your_name", update.getMessage().getText());
    }
}
