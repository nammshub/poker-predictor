package com.vdha.pokerpredictor.services;

import com.vdha.pokerpredictor.models.Carte;
import com.vdha.pokerpredictor.models.Couleur;
import com.vdha.pokerpredictor.models.Score;
import com.vdha.pokerpredictor.models.Valeur;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ScoreCalculatorService {

    private static final Set<Carte> QUINTE_FLUSH_ROYAL_PIQUE = Set.of(new Carte(Couleur.PIQUE, Valeur.AS), new Carte(Couleur.PIQUE, Valeur.ROI), new Carte(Couleur.PIQUE, Valeur.DAME), new Carte(Couleur.PIQUE, Valeur.VALET), new Carte(Couleur.PIQUE, Valeur.DIX));
    private static final Set<Carte> QUINTE_FLUSH_ROYAL_COEUR = Set.of(new Carte(Couleur.COEUR, Valeur.AS), new Carte(Couleur.COEUR, Valeur.ROI), new Carte(Couleur.COEUR, Valeur.DAME), new Carte(Couleur.COEUR, Valeur.VALET), new Carte(Couleur.COEUR, Valeur.DIX));
    private static final Set<Carte> QUINTE_FLUSH_ROYAL_CARREAU = Set.of(new Carte(Couleur.CARREAU, Valeur.AS), new Carte(Couleur.CARREAU, Valeur.ROI), new Carte(Couleur.CARREAU, Valeur.DAME), new Carte(Couleur.CARREAU, Valeur.VALET), new Carte(Couleur.CARREAU, Valeur.DIX));
    private static final Set<Carte> QUINTE_FLUSH_ROYAL_TREFLE = Set.of(new Carte(Couleur.TREFLE, Valeur.AS), new Carte(Couleur.TREFLE, Valeur.ROI), new Carte(Couleur.TREFLE, Valeur.DAME), new Carte(Couleur.TREFLE, Valeur.VALET), new Carte(Couleur.TREFLE, Valeur.DIX));
    private static final long MULTIPLICATEUR = 15;

    public long getScore(Set<Carte> cartes, Set<Carte> tapis) {
        Set<Carte> cartesTotales = Stream.concat(cartes.stream(), tapis.stream())
                .collect(Collectors.toSet());
        // si nbr cartesTotales == 2 (preflop) => on passe direct à la verif de pair
        if(cartesTotales.size() == 2){
            return verifiePaire(cartesTotales);
        }
        return verifieRoyalFlush(cartesTotales);
    }

    private long verifieRoyalFlush(Set<Carte> cartes) {
        if (cartes.size() > 4 && (cartes.containsAll(QUINTE_FLUSH_ROYAL_PIQUE) || cartes.containsAll(QUINTE_FLUSH_ROYAL_COEUR) || cartes.containsAll(QUINTE_FLUSH_ROYAL_CARREAU) || cartes.containsAll(QUINTE_FLUSH_ROYAL_TREFLE))) {
            return Score.QUINTE_FLUSH_ROYALE.getValeur();
        }
        return verifieQuinteFlush(cartes);
    }

    private long verifieQuinteFlush(Set<Carte> cartes) {
        int quinteFlushValue = giveSuiteValue(cartes, true);
        if (quinteFlushValue > 0) {
            return Score.QUINTE_FLUSH.getValeur() + quinteFlushValue;
        }
        return verifieCarre(cartes);
    }

    private long verifieCarre(Set<Carte> cartes) {
        int carreValue = giveSameValue(cartes, 4, List.of());
        if (carreValue > 0) {
            return Score.CARRE.getValeur() + carreValue * MULTIPLICATEUR + giveSameValue(cartes, 1, List.of(carreValue));
        }
        return verifieFull(cartes);
    }

    private long verifieFull(Set<Carte> cartes) {
        int trioValue = giveSameValue(cartes, 3, List.of());
        if (trioValue > 0) {
            int duoValue = giveSameValue(cartes, 2, List.of(trioValue));
            if (duoValue > 0) {
                return Score.FULL.getValeur() + trioValue * MULTIPLICATEUR + duoValue;
            }
        }
        return verifieCouleur(cartes);
    }

    private long verifieCouleur(Set<Carte> cartes) {
        int couleurValue = getCouleurValue(cartes);
        if (couleurValue > 0) {
            return Score.COULEUR.getValeur() + couleurValue;
        }
        return verifieQuinte(cartes);
    }

    private long verifieQuinte(Set<Carte> cartes) {
        int quinteValue = giveSuiteValue(cartes, false);
        if (quinteValue > 0) {
            return Score.QUINTE.getValeur() + quinteValue;
        }
        return verifieBrelan(cartes);
    }

    private long verifieBrelan(Set<Carte> cartes) {
        int brelanValue = giveSameValue(cartes, 3, List.of());
        if (brelanValue > 0) {
            return Score.BRELAN.getValeur() + brelanValue * MULTIPLICATEUR + giveSameValue(cartes, 1, List.of(brelanValue));
        }
        return verifieDoublePaire(cartes);
    }

    private long verifieDoublePaire(Set<Carte> cartes) {
        int firstPairValue = giveSameValue(cartes, 2, List.of());
        if (firstPairValue > 0) {
            int secondPairValue = giveSameValue(cartes, 2, List.of(firstPairValue));
            if (secondPairValue > 0) {
                return Score.DOUBLE_PAIR.getValeur() + firstPairValue * MULTIPLICATEUR * MULTIPLICATEUR + secondPairValue * MULTIPLICATEUR + giveSameValue(cartes, 1, List.of(firstPairValue, secondPairValue));
            }
        }
        return verifiePaire(cartes);
    }

    private long verifiePaire(Set<Carte> cartes) {
        int firstPairValue = giveSameValue(cartes, 2, List.of());
        if (firstPairValue > 0) {
            return Score.PAIR.getValeur() + firstPairValue * MULTIPLICATEUR + giveSameValue(cartes, 1, List.of(firstPairValue));
        }
        return verifieCarteHaute(cartes);
    }

    private long verifieCarteHaute(Set<Carte> cartes) {
        int carte1 = giveSameValue(cartes, 1, List.of());
        int carte2 = giveSameValue(cartes, 1, List.of(carte1));
        int carte3 = giveSameValue(cartes, 1, List.of(carte1,carte2));
        int carte4 = giveSameValue(cartes, 1, List.of(carte1,carte2, carte3));
        int carte5 = giveSameValue(cartes, 1, List.of(carte1,carte2, carte3, carte4));
        return (int) (carte1 * Math.pow(MULTIPLICATEUR,4) + carte2 * Math.pow(MULTIPLICATEUR,3) +carte3 * Math.pow(MULTIPLICATEUR,2) +carte4 * Math.pow(MULTIPLICATEUR,1) + carte5);
    }

    private int getCouleurValue(Set<Carte> cartes) {
        Map<Couleur, List<Carte>> map = cartes.stream().collect(Collectors.groupingBy(Carte::getCouleur));
        Optional<List<Carte>> sameColor = map.values().stream().filter(list -> list.size() >= 5).findFirst();
        // on cherche alors la carte dominante
        return sameColor.map(carteList -> carteList.stream().sorted(Comparator.comparing((Carte carte) -> carte.getValeur().getPuissance()).reversed()).map(carte -> carte.getValeur().getPuissance()).findFirst().orElse(0)).orElse(0);
    }

    private int giveSameValue(Set<Carte> cartes, int nbrRequis, List<Integer> valeursExclues) {
        List<Carte> cartesTriees = cartes.stream().sorted(Comparator.comparing((Carte carte) -> carte.getValeur().getPuissance()).reversed()).toList();
        int iterCarteAnalysee = 0;
        Carte carteCurr = cartesTriees.get(iterCarteAnalysee);
        int nbTrouve = 1;
        while (nbTrouve < nbrRequis && iterCarteAnalysee < cartesTriees.size()-1) {
            iterCarteAnalysee++;
            Carte carteAnalysee = cartesTriees.get(iterCarteAnalysee);
            if (carteCurr.getValeur().getPuissance() == carteAnalysee.getValeur().getPuissance() && !valeursExclues.contains(carteAnalysee.getValeur().getPuissance())) {
                nbTrouve++;
            } else {
                nbTrouve = 1;
            }
            carteCurr = carteAnalysee;
        }
        if (nbTrouve == nbrRequis) {
            return carteCurr.getValeur().getPuissance();
        }
        return 0;
    }

    private int giveSuiteValue(Set<Carte> setCartes, boolean sameColor) {
        List<List<Carte>> candidatsPotentiels = new ArrayList<>();
        Set<Carte> cartes = new HashSet<>();
        // dans le cas de la suite, un AS peut valoir 14 ou 1. Donc pour chaque As(14), on cree un As(1) au cas où
        for (Carte carte : setCartes) {
            if (carte.getValeur() == Valeur.AS) {
                Carte petitAs = new Carte(carte.getCouleur(), Valeur.PETIT_AS);
                cartes.add(petitAs);
            }
            cartes.add(carte);
        }
        if (sameColor) {
            Map<Couleur, List<Carte>> mapByColor = cartes.stream().collect(Collectors.groupingBy(Carte::getCouleur));
            List<List<Carte>> couleursValides = mapByColor.values().stream().filter(list -> list.size() >= 5).toList();
            candidatsPotentiels.addAll(couleursValides);
        } else {
            candidatsPotentiels.add(cartes.stream().toList());
        }
        // on trie par valeur desc
        for (List<Carte> listCandidat : candidatsPotentiels) {
            List<Carte> cartesTriees = listCandidat.stream().sorted(Comparator.comparing((Carte carte) -> carte.getValeur().getPuissance()).reversed()).toList();
            int iterCarteAnalysee = 0;
            Carte carteCurr = cartesTriees.get(iterCarteAnalysee);
            int firstValue = carteCurr.getValeur().getPuissance();
            int nbTrouve = 1;
            while (nbTrouve < 5 && iterCarteAnalysee < cartesTriees.size()-1) {
                iterCarteAnalysee++;
                Carte carteAnalysee = cartesTriees.get(iterCarteAnalysee);
                if (carteCurr.getValeur().getPuissance() == carteAnalysee.getValeur().getPuissance() + 1) {
                    nbTrouve++;
                } else {
                    nbTrouve = 1;
                    firstValue = carteAnalysee.getValeur().getPuissance();
                }
                carteCurr = carteAnalysee;
            }
            if (nbTrouve == 5) {
                return firstValue;
            }
        }
        return 0;
    }
}
