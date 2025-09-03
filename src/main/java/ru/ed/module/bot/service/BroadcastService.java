package ru.ed.module.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.ed.module.bot.model.Broadcast;
import ru.ed.module.bot.repository.BroadcastRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BroadcastService {

    private final BroadcastRepository repository;

    public Optional<Broadcast> get(UUID id) {
        return repository.findById(id);
    }

    public Broadcast save(Broadcast entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public List<Broadcast> list() {
        return repository.findAll();
    }

    public Page<Broadcast> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Broadcast> list(Pageable pageable, Specification<Broadcast> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
