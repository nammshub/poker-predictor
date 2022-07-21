package com.vdha.pokerpredictor.models;

import lombok.Getter;

@Getter
public enum Valeur {
    DEUX(2),TROIS(3),QUATRE(4),CINQ(5),SIX(6),SEPT(7),HUIT(8),NEUF(9),DIX(10),VALET(11),DAME(12),ROI(13),AS(14), PETIT_AS(1);

    private final int puissance;

    Valeur(int puissance){
        this.puissance= puissance;
    }
}
