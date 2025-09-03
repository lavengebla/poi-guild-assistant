package ru.ed.module.back.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.ed.module.back.model.Participant;
import ru.ed.module.back.repository.ParticipantRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantRepository repository;

    public Optional<Participant> get(UUID id) {
        return repository.findById(id);
    }

    public Optional<Participant> findByTelegramId(Long telegramId) {return repository.findByTelegramId(telegramId);}

    public Participant save(Participant entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<Participant> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Participant> list(Pageable pageable, Specification<Participant> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
