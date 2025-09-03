package ru.ed.module.bot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DialogType {

    REGISTRATION("Регистрация");

    private final String humanReadableName;


}
