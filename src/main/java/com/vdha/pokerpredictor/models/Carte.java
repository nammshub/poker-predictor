package com.vdha.pokerpredictor.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class Carte {
    private Couleur couleur;
    private Valeur valeur;
}
