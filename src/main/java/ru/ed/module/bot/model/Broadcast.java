package ru.ed.module.bot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import ru.ed.module.model.AbstractEntity;

@Entity(name = "broadcast_data")
@Getter
@Setter
public class Broadcast extends AbstractEntity {

    @Column(name = "event_name")
    private String eventName;

    @Column(name = "chat_id")
    private String chatId;

    @Column(name = "thread_id")
    private Integer threadId;
}
