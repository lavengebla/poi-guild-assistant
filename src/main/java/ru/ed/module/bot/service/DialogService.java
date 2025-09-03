package ru.ed.module.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.ed.module.bot.event.TelegramEventPublisher;
import ru.ed.module.bot.model.DialogEntity;
import ru.ed.module.bot.model.DialogSession;
import ru.ed.module.bot.model.DialogType;
import ru.ed.module.bot.repository.DialogEntityRepository;
import ru.ed.module.bot.repository.DialogSessionRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DialogService {

    private final DialogEntityRepository dialogEntityRepository;
    private final DialogSessionRepository dialogSessionRepository;

    private final TelegramEventPublisher publisher;

    /**
     * активация уже существующего диалога
     *
     * @param telegramId - id в телеграм
     * @param dialogType - тип диалога
     * @throws IllegalArgumentException - если не найдена сущность
     */
    public void activate(Long telegramId,
                         DialogType dialogType) throws IllegalArgumentException {
        List<DialogSession> sessions
            = dialogSessionRepository.findByTelegramId(telegramId);

        Optional<DialogSession> candidate
            = sessions
            .stream()
            .filter(s -> s.getDialogType().equals(dialogType))
            .findAny();

        if (candidate.isPresent()) {
            sessions
                .stream()
                .filter(s -> !s.getDialogType().equals(dialogType))
                .forEach(s -> {
                    s.setActive(false);
                    dialogSessionRepository.save(s);
                });
            DialogSession dialogSession = candidate.get();

            Optional<DialogEntity> lastQuestionOptional =
                dialogEntityRepository
                    .findByDialogSession(dialogSession)
                    .stream()
                    .filter(d -> d.getSubmission() == null)
                    .min(Comparator.comparing(DialogEntity::getOrder));

            if (lastQuestionOptional.isPresent()) {
                //todo задать вопрос
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    public DialogSession create(DialogSession session,
                                List<DialogEntity> entities) {
        List<DialogSession> userSessions
            = dialogSessionRepository.findByTelegramId(session.getTelegramId());

        userSessions
            .forEach(s -> {
                if (s.isActive()) {
                    s.setActive(false);
                    dialogSessionRepository.save(s);
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(session.getTelegramId());
                    sendMessage.setText("Текущий диалог [%s] переключен на [%s]. Для возвращения к диалогу введите команду /resume %s"
                        .formatted(
                            s.getDialogType().getHumanReadableName(),
                            session.getDialogType().getHumanReadableName(),
                            s.getDialogType().name()
                        )
                    );
                    publisher.publish(sendMessage);
                }
            });

        DialogSession dialogSession =
            dialogSessionRepository.save(session);

        entities.forEach(
            e -> {
                e.setDialogSession(dialogSession);
                dialogEntityRepository.save(e);
            }
        );



        return dialogSession;
    }

    public void cancel(Long telegramId) {
        List<DialogSession> userSessions
            = dialogSessionRepository.findByTelegramId(telegramId);

        userSessions
            .forEach(s -> {
                if (s.isActive()) {
                    s.setActive(false);
                    dialogSessionRepository.save(s);
                }
            });
    }
}
