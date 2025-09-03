package ru.ed.module.bot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import ru.ed.module.model.AbstractEntity;

@Entity(name = "media_group")
@Getter
@Setter
public class MediaGroup extends AbstractEntity {

    @Column(name = "media_group_id", unique = true)
    private String mediaGroupId;

}
