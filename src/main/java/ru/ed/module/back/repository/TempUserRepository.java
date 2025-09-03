package ru.ed.module.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ed.module.back.model.TempUser;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TempUserRepository extends JpaRepository<TempUser, UUID> {

    Optional<TempUser> findByTelegramId(Long id);

}
