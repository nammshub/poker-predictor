package com.vdha.pokerpredictor.models;

import lombok.Getter;

import java.util.*;

@Getter
public class Deck {
    private List<Carte> cartes;


    public void resetCartes() {
        cartes = new ArrayList<>();
        Arrays.stream(Couleur.values()).forEach(couleur -> Arrays.stream(Valeur.values()).forEach(valeur -> {
            if(valeur != Valeur.PETIT_AS) {
                Carte carte = new Carte(couleur, valeur);
                cartes.add(carte);
            }
        }));
    }

    public void removeCartes(Set<Carte> cartesToRemove) {
        cartes.removeIf(cartesToRemove::contains);
    }

    public Set<Carte> distributeCartes(int nbrCartes) {
        Set<Carte> cartesToReturn = new HashSet<>();
        Collections.shuffle(this.cartes);
        Iterator<Carte> iterator = this.cartes.iterator();
        while (iterator.hasNext() && cartesToReturn.size() < nbrCartes) {
            cartesToReturn.add(iterator.next());
            iterator.remove();
        }
        return cartesToReturn;
    }
}
