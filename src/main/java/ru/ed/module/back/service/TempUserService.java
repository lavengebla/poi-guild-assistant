package ru.ed.module.back.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ed.module.back.model.TempUser;
import ru.ed.module.back.model.TempUserResponse;
import ru.ed.module.back.repository.TempUserRepository;

@Service
@RequiredArgsConstructor
public class TempUserService {

    private final TempUserRepository repository;

    public TempUser create(Long telegramId) {
        TempUser tempUser = new TempUser(telegramId);
        return repository.save(tempUser);
    }

    public void addUserResponse(Long telegramId,
                                String question,
                                String response) {
        TempUser tempUser = repository.findByTelegramId(telegramId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        tempUser.getResponses().add(new TempUserResponse(tempUser, question, response));
        repository.save(tempUser);
    }
}
