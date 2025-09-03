package ru.ed.module.back.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import ru.ed.module.model.AbstractEntity;

@Entity(name = "character_data")
@Getter
@Setter
public class PoiCharacter extends AbstractEntity {

    @Column(name = "char_name")
    String name;

    @Column(name = "poi_id")
    String poiId;

    @Column(name = "char_bm")
    String bm;

    @Enumerated(EnumType.STRING)
    @Column(name = "multiplier")
    Multiplier multiplier;

    @Enumerated(EnumType.STRING)
    @Column(name = "spec")
    Spec spec;

}
