package ru.ed.module.bot.service.impl;

import org.springframework.stereotype.Service;
import ru.ed.module.bot.service.ImageService;

import java.io.InputStream;

@Service
public class ResourceImageService implements ImageService {
    @Override
    public InputStream getImage(String name) {
        return ResourceImageService.class.getClassLoader().getResourceAsStream("pictures/%s.jpg".formatted(name));
    }
}
