package com.vdha.pokerpredictor.services;

import com.vdha.pokerpredictor.exceptions.ValidatorException;
import com.vdha.pokerpredictor.models.Carte;
import com.vdha.pokerpredictor.models.GameConfig;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class GameConfigValidatorService {

    public void validate(GameConfig gameConfig) throws ValidatorException {
        List<String> erreurs = new ArrayList<>();
        // plusieurs controles à effectuer
        // nbrParties et adversaires
        if (gameConfig.getNbrPartiesSimulees() < 1) {
            erreurs.add("Le nombre de parties simulées doit etre strictement superieur à 0 !");
        }
        if (gameConfig.getNbrAdversaires() < 1) {
            erreurs.add("Le nombre d'adversaires doit etre strictement superieur à 0 !");
        }
        if(gameConfig.getCartesJoueur().size() != 2){
            erreurs.add("Le nombre de cartes du joueur doit etre egal à 2 !");
        }
        if(! ((gameConfig.getFlop().size() == 3) || (gameConfig.getFlop().isEmpty()))){
            erreurs.add("Le nombre de cartes du flop ne peut etre que 0 ou 3 !");
        }
        if(gameConfig.getTurn() != null && gameConfig.getFlop().size() != 3){
            erreurs.add("Le Flop est incorrect alors que la carte Turn est mentionnée !");
        }
        if(gameConfig.getRiver() != null && gameConfig.getTurn() == null){
            erreurs.add("La carte Turn est absente alors que la carte River est mentionnée !");
        }
        // verif d'unicite des cartes fournies
        List<Carte> cartesFournies = new ArrayList<>(gameConfig.getCartesJoueur());
        cartesFournies.addAll(gameConfig.getFlop());
        Carte turn = gameConfig.getTurn();
        Carte river = gameConfig.getRiver();
        if(turn != null){
            cartesFournies.add(turn);
        }
        if(river != null){
            cartesFournies.add(river);
        }
        Set<Carte> cartesFourniesSet = new HashSet<>(cartesFournies);
        if(cartesFourniesSet.size() != cartesFournies.size()){
            erreurs.add("Il y a des doublons dans les cartes mentionnées !");
        }


        if(!erreurs.isEmpty()){
            throw new ValidatorException(erreurs);
        }
    }
}
