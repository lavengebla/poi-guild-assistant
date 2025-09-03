package ru.ed.module.bot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import ru.ed.module.model.AbstractEntity;

@Entity(name = "dialog_entities")
@Getter
@Setter
public class DialogEntity extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "dialog_session_id")
    private DialogSession dialogSession;

    @Column(name = "processor")
    private String processor;

    @Column(name = "submission")
    private String submission;

    @Column(name = "order")
    private Integer order;

}
