package com.vanguard.demo.entity;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Hidden
public class CurrencyEntity extends BaseEntity {

    @Column(unique = true)
    private String attributeValue;
    
}
