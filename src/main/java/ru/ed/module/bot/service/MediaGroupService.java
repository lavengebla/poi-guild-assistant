package ru.ed.module.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ed.module.bot.model.MediaGroup;
import ru.ed.module.bot.repository.MediaGroupRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MediaGroupService {

    private final MediaGroupRepository mediaGroupRepository;

    public MediaGroup save(MediaGroup entity) {
        return mediaGroupRepository.save(entity);
    }

    public List<MediaGroup> findByCreatedAtBefore(LocalDateTime time) {
        return mediaGroupRepository.findByCreatedAtBefore(time);
    }

    public void deleteAll(List<MediaGroup> mediaGroupList) {
        mediaGroupRepository.deleteAll(mediaGroupList);
    }

}
