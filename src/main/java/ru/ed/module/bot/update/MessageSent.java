package ru.ed.module.bot.update;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.LinkPreviewOptions;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.ed.module.bot.command.Command;
import ru.ed.module.bot.command.Exec;
import ru.ed.module.bot.event.TelegramEventPublisher;
import ru.ed.module.bot.service.BroadcastService;
import ru.ed.module.bot.service.ImageService;
import ru.ed.module.bot.service.MediaGroupCollector;
import ru.ed.module.bot.service.TelegramBotImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Log4j2
@Component
@HandlerType(type = "message")
@RequiredArgsConstructor
public class MessageSent implements UpdateHandler {

    private final TelegramEventPublisher telegramEventPublisher;
    private final List<Command> commandList;
    private final TelegramBotImpl bot;
    private final BroadcastService broadcastService;
    private final MediaGroupCollector mediaGroupCollector;
    private final ImageService imageService;


    @Override
    public void handle(Update update) {
        Message message = update.getMessage();
        log.debug("обрабатываю сообщение: {}", message);
        if (message.getText() != null) {
            log.debug("сообщение содержит текст: {}", message.getText());
            textMessage(message);
        }
        if (message.getCaption() != null) {
            log.debug("сообщение с caption: {}", message.getText());
            caption(message);
        }

        if (message.getCaption() == null && message.getMediaGroupId() != null) {
            mediaGroupCollector.addUpdate(message);
        }
//        //новый чат мембер
//        else if (!update.getMessage().getNewChatMembers().isEmpty()) {
//            log.trace("сообщение о новом участнике чата");
//            newChatMembers(update.getMessage().getChat(), update.getMessage().getNewChatMembers());
//        }
//        //пользователь вышел
//        else if (update.getMessage().getLeftChatMember() != null) {
//            leftChatMember(update.getMessage().getChat(), update.getMessage().getLeftChatMember());
//        }
//        //новый заголовок
//        else if (update.getMessage().getNewChatTitle() != null) {
//            log.trace("сообщение об изменении заголовка");
//            changeChannelName(update.getMessage().getChat(), update.getMessage().getNewChatTitle());
//        }
//
//        //можно сделать тоглом, чтобы не регались автоматом
//        updateChatMember(update.getMessage().getChat(), update.getMessage().getFrom());
    }


    private void caption(Message message) {
        String caption = message.getCaption();
        if (message.getCaptionEntities() != null && hasRepostHashTag(message.getCaptionEntities())) {

            if (!checkLinkIsPresent(message)) {
                return;
            }

            broadcastService.list()
                .stream()
                .parallel()
                .forEach(b -> {
                    if (hasHashTag(message.getCaptionEntities(), b.getEventName())) {
                        if (message.getMediaGroupId() == null) {
                            SendPhoto sendPhoto = new SendPhoto();
                            try {
                                File photo = bot.getFile(message.getPhoto().getLast().getFileId());
                                InputFile inputFile = new InputFile();
                                inputFile.setMedia(photo);
                                sendPhoto.setPhoto(inputFile);
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e);
                            }
                            sendPhoto.setCaption(caption
                                .replace("#repost", "")
                                .replace("#%s".formatted(b.getEventName()), ""));
                            sendPhoto.setCaptionEntities(
                                message.getCaptionEntities()
                                    .stream()
                                    .filter(e -> !(e.getType().equals("hashtag") && e.getText().equals("#repost")))
                                    .filter(e -> !(e.getType().equals("hashtag") && e.getText().equals("#%s".formatted(b.getEventName()))))
                                    .toList()
                            );
                            sendPhoto.setChatId(b.getChatId());
                            sendPhoto.setMessageThreadId(b.getThreadId());
                            telegramEventPublisher.publish(sendPhoto);
                        } else {
                            mediaGroupCollector.addUpdate(message);
                        }
                    }
                });
        }
    }

    private boolean checkLinkIsPresent(Message message) {
        List<MessageEntity> messageEntities = new ArrayList<>();
        if (message.hasEntities()) {
            messageEntities = message.getEntities();
        } else if (message.getCaptionEntities() != null && !message.getCaptionEntities().isEmpty()) {
            messageEntities = message.getCaptionEntities();
        }
        boolean isPresent = messageEntities
            .stream()
            .anyMatch(e -> e.getType().equals("text_link") && e.getUrl().equals("https://t.me/poi160"));

        if (!isPresent) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(message.getFrom().getId());
            sendMessage.setText("Ты забыл указать ссылку на чат AURI в посте");
            sendMessage.setReplyToMessageId(message.getMessageId());
            telegramEventPublisher.publish(sendMessage);

            SendMessage pattern = new SendMessage();
            pattern.setChatId(message.getFrom().getId());
            pattern.setText("""
                ———<b>
                Автор: </b><a href="tg://user?id=%s">%s %s</a>%s
                <b><a href="https://t.me/poi160">AURI | Обучаемся, чтобы обучать</a></b>
                """.formatted(
                message.getFrom().getId(),
                message.getFrom().getFirstName(),
                message.getFrom().getLastName(),
                message.getFrom().getUserName() != null ?
                    " (@%s)".formatted(message.getFrom().getUserName()) : "")
            );
            pattern.setReplyToMessageId(message.getMessageId());
            pattern.enableHtml(true);
            LinkPreviewOptions previewOptions = new LinkPreviewOptions();
            previewOptions.setIsDisabled(true);
            pattern.setLinkPreviewOptions(previewOptions);
            telegramEventPublisher.publish(pattern);
        }

        return isPresent;
    }

    /**
     * обработчик - пользователь отправил сообщение
     */
    private void textMessage(Message message) {
        String text = message.getText();
        if (text.startsWith("/")) {
            processCommand(message, text);
        } else {

            if (!message.getChatId().toString().startsWith("-")) {
                ifWhatCanIdo(message);
                ifRegisterParticipant(message);
            }
            
            if (message.hasEntities() && hasRepostHashTag(message.getEntities())) {
                hasRepost(message);
            }
        }
    }

    private void ifRegisterParticipant(Message message) {
        if (message.getText().equals("Регистрация участника")) {
            SendPhoto regIntro = new SendPhoto();
            regIntro.setChatId(message.getChatId());
            InputFile inputFile = new InputFile();
            inputFile.setMedia(imageService.getImage("reg_first"), "reg_first");
            regIntro.setPhoto(inputFile);
            regIntro.setParseMode("html");
            regIntro.setCaption(
                """
                    Я очень рад, что именно ты — <b>%s</b>, будешь играть с нами! Для регистрации ознакомься с <a href="https://t.me/poi160/3773/175263">Правилами клана</a>, это важно, чтобы избежать недопониманий в будущем.\s
                    
                    Если правила устраивают - посмотри на картинку, тут я показал где ты сможешь взять информацию в самой игре и мы пройдем 5 этапа:
                    
                    🟢1. Твое настоящее Имя;
                    🟢2. Скрин экрана «Настройки»;
                    🟢3. Твой игровой Ник;
                    🟢4. Класс персонажа;
                    🟢5. ID персонажа;
                    🤩6. Аватар персонажа (картинка, с которой ты ассоциируешь персонажа или которая тебе просто нравится).
                    
                    Приступим?
                    """.formatted(message.getFrom().getFirstName()));

            InlineKeyboardMarkup accept = new InlineKeyboardMarkup();
            InlineKeyboardButton acceptBtn = new InlineKeyboardButton();
            acceptBtn.setText("Правила устраивают");
            acceptBtn.setCallbackData("/rules_accept");
            accept.setKeyboard(List.of(List.of(acceptBtn)));
            regIntro.setReplyMarkup(accept);

            telegramEventPublisher.publish(regIntro);
        }
    }

    private void ifWhatCanIdo(Message message) {
        if (message.getText().equals("Что я умею?")) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(message.getChatId());
            sendMessage.setMessageThreadId(message.getMessageThreadId());
            sendMessage.enableHtml(true);
            sendMessage.setText("""
            <b>Что я умею?</b>
            <b>— 📝Регистрация</b> - я могу тебя зарегистрировать, как члена клана <b>AcademAURI</b>, только прошедшие отбор Бессмертные переводятся в клан <b>AURI</b> по рекомендации своих Наставников или Офицеров.
            <b>— 🪪Мой профиль</b> - тут ты сможешь получить самую актуальную информацию о себе, своем наставнике, проинформировать меня о смене Никнейма или Класса, просмотреть историю по сезонам участия в Вестниках, Экспедициях и Командных аренах, а также - обновить свой Аватар в любую секунду.
            <blockquote>Помимо основных функций - я буду тебе передавать важные сообщения и напоминания от клана, собирать твою статистику по игровым активностям и напоминать когда и что тебе нужно сделать!</blockquote>
            """);
            telegramEventPublisher.publish(sendMessage);

            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(message.getChatId());
            deleteMessage.setMessageId(message.getMessageId());
            telegramEventPublisher.publish(deleteMessage);
        }
    }

    private void hasRepost(Message message) {
        broadcastService.list()
            .stream()
            .parallel()
            .forEach(b -> {
                if (hasHashTag(message.getEntities(), b.getEventName())) {
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setEntities(message.getEntities());
                    sendMessage.setLinkPreviewOptions(message.getLinkPreviewOptions());
                    sendMessage.setText(message.getText()
                        .replace("#repost", "")
                        .replace("#%s".formatted(b.getEventName()), ""));
                    sendMessage.setChatId(b.getChatId());
                    sendMessage.setMessageThreadId(b.getThreadId());
                    telegramEventPublisher.publish(sendMessage);
                }
            });
    }

    //todo в утилитарный метод
    public static boolean hasHashTag(List<MessageEntity> entities,
                                     String hashtag) {
        return entities.stream()
            .filter(e -> e.getType().equals("hashtag"))
            .anyMatch(e -> e.getText().equalsIgnoreCase("#%s".formatted(hashtag)));
    }

    private static boolean hasRepostHashTag(List<MessageEntity> entities) {
        return entities.stream()
            .filter(e -> e.getType().equals("hashtag"))
            .anyMatch(e -> e.getText().equalsIgnoreCase("#repost"));
    }

    private void processCommand(Message message,
                                String text) {
        log.debug("Обработка сообщения команды");
        Optional<Command> candidate = findCommand(text);
        if (candidate.isPresent()) {
            executeCommand(message, candidate.get());
        } else {
            //          commandNotFound(message);
        }
        log.debug("Обработка сообщения команды завершено");
    }

    private void executeCommand(Message message,
                                Command command) {
        if (command.requiresAdminRights()) {
            Long chatId = message.getChatId();
            Long userId = message.getFrom().getId();
            boolean isUserAdmin;
            try {
                isUserAdmin = bot.getChatAdministrator(chatId)
                    .stream()
                    .anyMatch(c -> c.getUser().getId().equals(userId));
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
            if (isUserAdmin) {
                command.execute(message);
            } else {
                commandShouldBeSentByAdmin(message);
            }
        } else {
            command.execute(message);
        }
    }

    private Optional<Command> findCommand(String text) {
        return commandList.stream()
            .filter(c -> {
                if (c.getClass().isAnnotationPresent(Exec.class)) {
                    Exec exec = c.getClass().getAnnotation(Exec.class);
                    return (
                        text.startsWith("/%s".formatted(exec.executable()))
                            || Arrays.stream(exec.alias()).anyMatch(a -> text.startsWith("/%s".formatted(a)))
                    );
                } else {
                    return false;
                }
            })
            .findFirst();
    }

    private void commandShouldBeSentByAdmin(org.telegram.telegrambots.meta.api.objects.Message message) {
        sendMessage(message, "команда может быть запущена только администратором чата");
    }

    private void commandNotFound(org.telegram.telegrambots.meta.api.objects.Message message) {
        sendMessage(message, "команда не найдена");
    }

    private void sendMessage(org.telegram.telegrambots.meta.api.objects.Message message,
                             String error) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(error);
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setChatId(message.getChatId());
        sendMessage.setMessageThreadId(message.getMessageThreadId());
        telegramEventPublisher.publish(sendMessage);
    }


//    private void newChatMembers(Chat chat,
//                                List<User> newChatMembers) {
//        newChatMembers.forEach(u -> updateChatMember(chat, u));
//    }

//    private void updateChatMember(Chat chat,
//                                  User user) {
//        log.trace("обновляю сообщение о пользователе [{}] в чате [{}]", user, chat);
//        Optional<UserEntity> candidate = userService.findByTelegramId(user.getId());
//        log.trace("предполагаемый участник найден - {}", candidate.isPresent());
//        UserEntity participant;
//        participant = candidate.orElseGet(() -> userService.add(user.getUserName(), user.getId()));
//        log.trace("внутренняя запись о пользователе: [{}]", participant);
//        if (!user.getUserName().equals(participant.getUsername())) {
//            log.trace("обновляю username");
//            participant.setUsername(user.getUserName());
//            userService.update(participant);
//        }
//
//        Optional<ChannelEntity> channel = channelService.find(chat.getId());
//        if (channel.isPresent()) {
//            log.trace("обновляю список участников чата");
//            List<UserEntity> participants =
//                channelService.findParticipants(channel.get());
//            log.trace("список участников чата {}", participants);
//            List<Long> usersId = participants.stream().map(UserEntity::getId).toList();
//            if (!usersId.contains(participant.getId())) {
//                log.trace("участника нет в списке, добавляю");
//                channelService.addParticipant(participant, channel.get());
//            }
//        } else {
//            log.error("failed to add participant, chat is not registered - [{}]", chat);
//        }
//    }
//
//    private void leftChatMember(Chat chat,
//                                User user) {
//        Optional<UserEntity> candidate = userService.findByTelegramId(user.getId());
//        if (candidate.isPresent()) {
//            Optional<ChannelEntity> channel = channelService.find(chat.getId());
//            channel
//                .ifPresent(
//                    entity -> channelService.deleteParticipant(candidate.get(), entity)
//                );
//        }
//    }
//
//    private void changeChannelName(Chat chat,
//                                   String title) {
//        //как-то обрабатываем событие
//        Optional<ChannelEntity> candidate = channelService.find(chat.getId());
//        if (candidate.isPresent()) {
//            ChannelEntity channel = candidate.get();
//            channel.setName(title);
//            channelService.update(channel);
//        }
//    }
}
