package ru.ed.module.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.ed.module.back.model.Participant;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, UUID>,
    JpaSpecificationExecutor<Participant> {
    Optional<Participant> findByTelegramId(Long telegramId);
}
