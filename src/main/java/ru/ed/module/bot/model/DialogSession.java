package ru.ed.module.bot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.ed.module.model.AbstractEntity;

@Entity(name = "dialog_session")
@Getter
@Setter
@NoArgsConstructor
public class DialogSession extends AbstractEntity {

    @Column(name = "telegram_id", nullable = false)
    private Long telegramId;

    @Column(name = "dialog_type", nullable = false)
    private DialogType dialogType;

    @Column(name = "is_active")
    private boolean active;

}
