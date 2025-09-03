package ru.ed.module.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.ed.module.bot.model.Broadcast;
import ru.ed.module.bot.model.MediaGroup;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface MediaGroupRepository extends JpaRepository<MediaGroup, UUID>{

    List<MediaGroup> findByCreatedAtBefore(LocalDateTime createdAt);

}
