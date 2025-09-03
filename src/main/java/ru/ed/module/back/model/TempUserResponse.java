package ru.ed.module.back.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import ru.ed.module.model.AbstractEntity;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user_responses")
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TempUserResponse extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private TempUser user;

    private String question;
    private String answer;


}
