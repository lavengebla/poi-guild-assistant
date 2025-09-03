package ru.ed.module.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.ed.module.bot.model.Broadcast;

import java.util.UUID;

@Repository
public interface BroadcastRepository extends JpaRepository<Broadcast, UUID>,
    JpaSpecificationExecutor<Broadcast> {
}
