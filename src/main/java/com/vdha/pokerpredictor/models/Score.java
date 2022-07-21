package com.vdha.pokerpredictor.models;

import lombok.Getter;

@Getter
public enum Score {
    QUINTE_FLUSH_ROYALE(10),QUINTE_FLUSH(9),CARRE(8),FULL(7), COULEUR(6), QUINTE(5),
    BRELAN(4), DOUBLE_PAIR(3), PAIR(2);

    private final long valeur;
    private static final int BASE = 1000000000;


    Score(long valeur) {
        this.valeur = valeur * BASE;
    }
}
