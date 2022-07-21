# poker-predictor
REST service to predict succes rate for each step according to game config

# HOW TO
- launch the REST service
- call the REST service with GET:[localhost:8080]/predict
- you need a JSON body to represent the game config such as:

```json

{
    "nbrPartiesSimulees": 5000,
    "nbrAdversaires": 6,
    "cartesJoueur": [
        {
            "valeur": "DEUX",
            "couleur": "CARREAU"
        },
        {
            "valeur": "QUATRE",
            "couleur": "CARREAU"
        }
    ],
    "flop": [
        {
            "valeur": "DEUX",
            "couleur": "CARREAU"
        },
        {
            "valeur": "QUATRE",
            "couleur": "CARREAU"
        },
        {
            "valeur": "DEUX",
            "couleur": "CARREAU"
        }
    ],
    "turn": {
        "valeur": "DEUX",
        "couleur": "CARREAU"
    },
    "river": {
        "valeur": "DEUX",
        "couleur": "CARREAU"
    }
}

```

# explanation: 
    -you can modify nbr of games simulated (3000 - 10000 seems a good range) and opponents
    -you must give your two cards
    -choices of colors: PIQUE, COEUR, CARREAU, TREFLE
    -choices of values: AS, ROI, DAME, VALET, DIX, NEUF, HUIT, SEPT, SIX, CINQ, QUATRE, TROIS, DEUX
    -the flop can be empty: you juste type []
    -the turn and river can be null
    
# example output
each result gives the odds of wins in %

```json
{
    "preFlopWinRate": 0.24,
    "flopWinRate": 15.48,
    "turnWinRate": 17.44,
    "riverWinRate": 20.14
}
```
