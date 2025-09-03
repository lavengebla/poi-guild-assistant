package ru.ed.module.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.ed.module.back.model.PoiCharacter;

import java.util.UUID;

@Repository
public interface PoiCharacterRepository extends JpaRepository<PoiCharacter, UUID>, JpaSpecificationExecutor<PoiCharacter> {
}
