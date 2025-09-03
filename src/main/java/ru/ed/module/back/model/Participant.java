package ru.ed.module.back.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.ed.module.model.AbstractEntity;

@Entity(name = "participant_data")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Participant extends AbstractEntity {

    @Column(name = "telegram_id", unique = true, nullable = false)
    Long telegramId;

    @Column(name = "username")
    String username;

    @Column(name = "alternative")
    String alternative;

    @Column(name = "is_admin")
    boolean admin;

    @Column(name = "is_accessible")
    boolean accessible;

}
