package ru.ed.module.back.model;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.ed.module.mapper.MapToJsonConverter;
import ru.ed.module.model.AbstractEntity;

import java.util.Map;

@Entity(name = "reg_tmp")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterTmp extends AbstractEntity {

    @Column(name = "telegram_id", unique = true, nullable = false)
    Long telegramId;

    @Column(name = "username")
    String username;

    @Column(name = "alternative")
    String alternative;

    @Column(name = "real_name")
    String realName;

    @Column(name = "settings_screen")
    String settingsScreen;

    @Column(name = "in_game_name")
    String inGameName;

    @Column(name = "in_game_spec")
    String inGameSpec;

    @Column(name = "avatar")
    String avatar;
}
