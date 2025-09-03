package ru.ed.module.bot.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.ed.module.back.service.ParticipantService;
import ru.ed.module.bot.event.TelegramEventPublisher;
import ru.ed.module.bot.service.ImageService;

import java.util.Collections;

@Component
@Exec(executable = "start")
@RequiredArgsConstructor
public class StartCommand implements Command {

    private final TelegramEventPublisher telegramEventPublisher;
    private final ImageService imageService;

    private final ParticipantService participantService;

    /*
    получение id
     */

    @Override
    public void execute(Message message) {
        SendPhoto startMessage = new SendPhoto();
        startMessage.setChatId(message.getChatId());

        InputFile inputFile = new InputFile();
        inputFile.setMedia(imageService.getImage("start"), "start");
        startMessage.setPhoto(inputFile);
        startMessage.setParseMode("html");
        startMessage.setCaption(
            """
                Здрям, <b>%s</b> - я милый бот клана <b>AURI</b>!
                
                <b>Я обучаюсь, чтобы обучать</b>❤️
                Не сердись на меня, если я что-то не знаю, в ближайшем будущем я смогу тебе помогать и по другим вопросам, ответы на которые я пока только изучаю.
                
                Если я не смог тебе помочь, пожалуйста, перейди <a href="https://t.me/poi160/189/168586">сюда</a> и мудрые члены клана расскажут тебе все, что знают🕵️‍♂️
                
                В скором времени я смогу тебе помогать, даже если ты не в клане <b><a href="https://t.me/poi160">AURI</a></b>, потерпи немного... Со мной будет весело, обещаю!
                """.formatted(message.getFrom().getFirstName()));
        ReplyKeyboardMarkup reply = new ReplyKeyboardMarkup();

        KeyboardRow row = new KeyboardRow();

        if (participantService.findByTelegramId(message.getFrom().getId()).isEmpty()) {
            KeyboardButton regButton = new KeyboardButton();
            regButton.setText("Регистрация участника");
            row.add(regButton);
        }

        KeyboardButton myAbilities = new KeyboardButton();
        myAbilities.setText("Что я умею?");


        row.add(myAbilities);

        reply.setKeyboard(Collections.singletonList(row));
        reply.setResizeKeyboard(true);

        startMessage.setReplyMarkup(reply);
        telegramEventPublisher.publish(startMessage);
    }

    @Override
    public boolean requiresAdminRights() {
        return false;
    }
}
