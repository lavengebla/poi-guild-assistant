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
@DialogPartProcessor("your_nickname")
public class YourNickProcessor extends DialogProcessor {

    public YourNickProcessor(ImageService imageService,
                             TelegramEventPublisher telegramEventPublisher,
                             TempUserService tempUserService) {
        super(imageService, telegramEventPublisher, tempUserService);
    }

    @Override
    public void proceedQuestion(DialogEntity entity) {
        SendPhoto yourNickName = new SendPhoto();
        yourNickName.setChatId(entity.getDialogSession().getTelegramId());

        InputFile inputFile = new InputFile();
        inputFile.setMedia(imageService.getImage("mock"), "mock");
        yourNickName.setPhoto(inputFile);
        yourNickName.setParseMode("html");
        yourNickName.setCaption(
                """
                        ⚪️Твой Ник⚪️⚪️⚪️
                        Впиши свой игровой Ник. Учти, что он должен быть точь в точь, как в игре!
                        """
        );
        ForceReplyKeyboard forceReplyKeyboard = new ForceReplyKeyboard();
        yourNickName.setReplyMarkup(forceReplyKeyboard);

        telegramEventPublisher.publish(yourNickName);
    }

    @Override
    public void handleResponse(Update update) {
        tempUserService.addUserResponse(update.getMessage().getChatId(), "your_nickname", update.getMessage().getText());
    }
}
