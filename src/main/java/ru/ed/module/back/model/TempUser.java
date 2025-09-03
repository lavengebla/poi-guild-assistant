package ru.ed.module.back.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.ed.module.model.AbstractEntity;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "reg_tmp")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TempUser extends AbstractEntity {

    @Column(name = "telegram_id", unique = true, nullable = false)
    Long telegramId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TempUserResponse> responses = new ArrayList<>();

    public TempUser(Long telegramId) {
        this.telegramId = telegramId;
    }
}
