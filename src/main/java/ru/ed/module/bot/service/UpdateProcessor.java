package ru.ed.module.bot.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.ed.module.bot.event.UpdateReceivedEvent;
import ru.ed.module.bot.update.HandlerType;
import ru.ed.module.bot.update.UpdateHandler;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateProcessor {

    private final List<UpdateHandler> updateHandlers;

    @EventListener
    private void handleUpdate(UpdateReceivedEvent event) {
        Optional<UpdateHandler> handlerCandidate = lookUpHandler(event.getUpdate());
        if (handlerCandidate.isPresent()) {
            log.trace("handler found...");
            log.trace("executing...");
            handlerCandidate.get().handle(event.getUpdate());
            log.trace("execution finished...");
        } else {
            log.trace("handler not found...");
            log.trace("skipping");
        }
    }

    private Optional<UpdateHandler> lookUpHandler(Update update) {
        Optional<Field> candidateField = Arrays.stream(update.getClass().getDeclaredFields())
            .filter(f -> f.isAnnotationPresent(JsonProperty.class))
            .filter(f -> !f.getName().equals("updateId"))
            .filter(f -> isFieldNull(f, update))
            .findFirst();

        return candidateField.flatMap(field -> updateHandlers
            .stream()
            .filter(h -> h.getClass().getAnnotation(HandlerType.class).type().equals(field.getName()))
            .findFirst());
    }

    private boolean isFieldNull(Field field, Object object) {
        try {
            field.setAccessible(true);
            return field.get(object) != null;
        } catch (IllegalAccessException e) {
            log.error("Couldn't to get value from field ", e);
            return false;
        }
    }

}
