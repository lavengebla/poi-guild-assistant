package ru.ed.module.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ed.module.back.model.RegisterTmp;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RegisterTmpRepository extends JpaRepository<RegisterTmp, UUID> {

    Optional<RegisterTmp> findByTelegramId(Long id);

}
