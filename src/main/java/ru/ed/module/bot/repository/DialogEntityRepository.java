package ru.ed.module.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ed.module.bot.model.DialogEntity;
import ru.ed.module.bot.model.DialogSession;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface DialogEntityRepository extends JpaRepository<DialogEntity, UUID> {

    List<DialogEntity> findByCreatedAtBefore(LocalDateTime createdAt);

    List<DialogEntity> findByDialogSession(DialogSession dialogSession);
}
