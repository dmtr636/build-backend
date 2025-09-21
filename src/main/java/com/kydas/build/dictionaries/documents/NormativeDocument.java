package com.kydas.build.dictionaries.documents;

import com.kydas.build.core.crud.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "normative_documents")
public class NormativeDocument extends BaseEntity {
    @Column(nullable = false)
    private String regulation;

    @Column(nullable = false)
    private String name;
}
