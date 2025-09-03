package ru.ed.module.bot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.ed.module.bot.event.TelegramEventPublisher;
import ru.ed.module.bot.model.MediaGroup;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static ru.ed.module.bot.update.MessageSent.hasHashTag;

@Slf4j
@Service
@RequiredArgsConstructor
public class MediaGroupCollector {

    private final MediaGroupService mediaGroupService;
    private final BroadcastService broadcastService;
    private final TelegramEventPublisher telegramEventPublisher;

    private final Map<String, List<Message>> mediaGroupUpdates = new HashMap<>();
    private final TelegramBotImpl bot;

    public void addUpdate(Message message) {
        try {
            MediaGroup mediaGroup = new MediaGroup();
            mediaGroup.setMediaGroupId(message.getMediaGroupId());
            mediaGroupService.save(mediaGroup);
        } catch (Exception ignore) {
        }
        mediaGroupUpdates.putIfAbsent(message.getMediaGroupId(), new ArrayList<>());
        mediaGroupUpdates.get(message.getMediaGroupId()).add(message);
    }

    @Scheduled(fixedDelay = 30L, timeUnit = TimeUnit.SECONDS)
    public void collectAndSend() {
        List<MediaGroup> mediaGroupList =
            mediaGroupService.findByCreatedAtBefore(LocalDateTime.now().minusSeconds(30L));

        mediaGroupList
            .forEach(this::postAlbum);

        mediaGroupService.deleteAll(mediaGroupList);
    }

    private void postAlbum(MediaGroup mediaGroup) {

        List<InputMedia> inputMedia = new ArrayList<>();
        Set<String> addedImages = new HashSet<>();

        List<Message> updates = mediaGroupUpdates.get(mediaGroup.getMediaGroupId());

        updates
            .forEach(m -> {
                try {
                    InputMedia inputMediaPhoto = new InputMediaPhoto();
                    PhotoSize photoSize = m.getPhoto().getLast();
                    if (!addedImages.contains(photoSize.getFileId())) {
                        File mediaFile = bot.getFile(photoSize.getFileId());
                        inputMediaPhoto.setMedia(mediaFile, mediaFile.getName());
                        inputMedia.add(inputMediaPhoto);
                        addedImages.add(photoSize.getFileId());
                    }
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            });

        Message message = updates.getFirst();

        broadcastService
            .list()
            .stream()
            .parallel()
            .forEach(b -> {
                if (hasHashTag(message.getCaptionEntities(), b.getEventName())) {

                    SendMediaGroup sendMediaGroup = new SendMediaGroup();
                    inputMedia.getFirst().setCaption(message.getCaption()
                        .replace("#repost", "")
                        .replace("#%s".formatted(b.getEventName()), ""));

                    inputMedia.getFirst().setCaptionEntities(
                        message.getCaptionEntities()
                            .stream()
                            .filter(e -> !(e.getType().equals("hashtag") && e.getText().equals("#repost")))
                            .filter(e -> !(e.getType().equals("hashtag") && e.getText().equals("#%s".formatted(b.getEventName()))))
                            .toList()
                    );

                    sendMediaGroup.setChatId(b.getChatId());
                    sendMediaGroup.setMessageThreadId(b.getThreadId());
                    sendMediaGroup.setMedias(inputMedia);
                    telegramEventPublisher.publish(sendMediaGroup);
                }
            });

        mediaGroupUpdates.remove(mediaGroup.getMediaGroupId());
    }


}
