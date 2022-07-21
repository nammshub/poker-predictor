package com.vdha.pokerpredictor.services;

import com.vdha.pokerpredictor.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SimulatorService {

    @Autowired
    ScoreCalculatorService scoreCalculatorService;

    private static final double CENT = 100;

    public ProbabilityResults predict(GameConfig gameConfig) {
        //on va simuler un nombre de parties determine et enregister les win/loses
        Map<Step, Integer> mapStepVictoires = simulerParties(gameConfig);

        //on injecte les resultats dans notre objet ProbabilityResults
        ProbabilityResults probabilityResult = new ProbabilityResults();
        Integer preflopWins = mapStepVictoires.get(Step.PREFLOP);
        if (preflopWins != null) {
            probabilityResult.setPreFlopWinRate(preflopWins * CENT / gameConfig.getNbrPartiesSimulees());
        }
        Integer flopWins = mapStepVictoires.get(Step.FLOP);
        if (flopWins != null) {
            probabilityResult.setFlopWinRate(flopWins * CENT / gameConfig.getNbrPartiesSimulees());
        }
        Integer turnWins = mapStepVictoires.get(Step.TURN);
        if (turnWins != null) {
            probabilityResult.setTurnWinRate(turnWins * CENT / gameConfig.getNbrPartiesSimulees());
        }
        Integer riverWins = mapStepVictoires.get(Step.RIVER);
        if (riverWins != null) {
            probabilityResult.setRiverWinRate(riverWins * CENT / gameConfig.getNbrPartiesSimulees());
        }

        return probabilityResult;
    }

    private Map<Step, Integer> simulerParties(GameConfig gameConfig) {
        Map<Step, Integer> mapWins = initializeMapWins();
        for (int i = 0; i < gameConfig.getNbrPartiesSimulees(); i++) {
            simulerPartie(gameConfig, mapWins);
        }
        return mapWins;
    }

    private void simulerPartie(GameConfig gameConfig, Map<Step, Integer> mapWins) {
        Deck deck = new Deck();
        deck.resetCartes();
        // on enleve les deux cartes du joueur en cours
        deck.removeCartes(gameConfig.getCartesJoueur());
        // si on a deja des cartes sur le tapis, on les enleve du deck
        Set<Carte> tapis = extractCartesToTapis(deck, gameConfig);
        // on distribue deux cartes à chaque adversaire
        List<Set<Carte>> cartesAdversaires = distribueCartesAdversaires(deck, gameConfig);
        // selon la config on va simuler tel ou tel step du tour
        if (tapis.isEmpty()) {
            simulerStep(Step.PREFLOP, gameConfig, cartesAdversaires, tapis, mapWins);
            tapis.addAll(deck.distributeCartes(3));
        }
        if(tapis.size() == 3){
            simulerStep(Step.FLOP, gameConfig, cartesAdversaires, tapis, mapWins);
            tapis.addAll(deck.distributeCartes(1));
        }
        if(tapis.size() == 4){
            simulerStep(Step.TURN, gameConfig, cartesAdversaires, tapis, mapWins);
            tapis.addAll(deck.distributeCartes(1));
        }
        if(tapis.size() == 5){
            simulerStep(Step.RIVER, gameConfig, cartesAdversaires, tapis, mapWins);
        }
    }

    private List<Set<Carte>> distribueCartesAdversaires(Deck deck, GameConfig gameConfig) {
        List<Set<Carte>> cartesAdversaires = new ArrayList<>(gameConfig.getNbrAdversaires());
        for (int i = 0; i < gameConfig.getNbrAdversaires(); i++) {
            cartesAdversaires.add(deck.distributeCartes(2));
        }
        return cartesAdversaires;
    }

    private Set<Carte> extractCartesToTapis(Deck deck, GameConfig gameConfig) {
        Set<Carte> tapis = new HashSet<>();

        if (gameConfig.getFlop().isEmpty()) {
            return tapis;
        }
        tapis.addAll(gameConfig.getFlop());
        deck.removeCartes(gameConfig.getFlop());

        if (gameConfig.getTurn() == null) {
            return tapis;
        }
        tapis.add(gameConfig.getTurn());
        deck.removeCartes(Set.of(gameConfig.getTurn()));

        if (gameConfig.getRiver() == null) {
            return tapis;
        }
        tapis.add(gameConfig.getRiver());
        deck.removeCartes(Set.of(gameConfig.getRiver()));

        return tapis;
    }

    private void simulerStep(Step step, GameConfig gameConfig, List<Set<Carte>> cartesAdversaires, Set<Carte> tapis, Map<Step, Integer> mapWins) {
        long meilleurScoreAdversaire = cartesAdversaires.stream().map(cartes -> scoreCalculatorService.getScore(cartes, tapis)).max(Long::compare).orElse(0L);
        long scoreJoueur = scoreCalculatorService.getScore(gameConfig.getCartesJoueur(), tapis);
        if(scoreJoueur >= meilleurScoreAdversaire){
            mapWins.put(step, mapWins.get(step) + 1);
        }
    }

    private Map<Step, Integer> initializeMapWins() {
        Map<Step, Integer> map = new HashMap<>();
        map.put(Step.PREFLOP, 0);
        map.put(Step.FLOP, 0);
        map.put(Step.TURN, 0);
        map.put(Step.RIVER, 0);
        return map;
    }
}
