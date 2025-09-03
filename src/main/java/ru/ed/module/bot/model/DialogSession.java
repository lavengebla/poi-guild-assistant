package ru.ed.module.bot.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.ed.module.back.model.TempUserResponse;
import ru.ed.module.model.AbstractEntity;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "dialogSession", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DialogEntity> entities = new ArrayList<>();

}
