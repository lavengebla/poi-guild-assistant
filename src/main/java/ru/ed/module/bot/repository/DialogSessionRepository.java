package ru.ed.module.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ed.module.bot.model.DialogSession;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface DialogSessionRepository extends JpaRepository<DialogSession, UUID> {

    List<DialogSession> findByTelegramId(Long telegramId);
    List<DialogSession> findByCreatedAtBefore(LocalDateTime createdAt);

}
