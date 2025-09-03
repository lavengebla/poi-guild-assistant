package ru.ed.module.bot.service;

import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public interface ImageService {

    InputStream getImage(String name);

}
