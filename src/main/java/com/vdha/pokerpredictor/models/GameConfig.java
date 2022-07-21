package com.vdha.pokerpredictor.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class GameConfig {
    int nbrPartiesSimulees;
    int nbrAdversaires;
    Set<Carte> cartesJoueur;
    Set<Carte> flop;
    Carte turn;
    Carte river;
}
