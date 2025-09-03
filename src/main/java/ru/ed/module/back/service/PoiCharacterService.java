package ru.ed.module.back.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.ed.module.back.model.PoiCharacter;
import ru.ed.module.back.repository.PoiCharacterRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PoiCharacterService {

    private final PoiCharacterRepository repository;

    public Optional<PoiCharacter> get(UUID id) {
        return repository.findById(id);
    }

    public PoiCharacter save(PoiCharacter entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<PoiCharacter> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<PoiCharacter> list(Pageable pageable, Specification<PoiCharacter> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
