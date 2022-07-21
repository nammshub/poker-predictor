package com.vdha.pokerpredictor.controllers;

import com.vdha.pokerpredictor.exceptions.ValidatorException;
import com.vdha.pokerpredictor.models.GameConfig;
import com.vdha.pokerpredictor.models.ProbabilityResults;
import com.vdha.pokerpredictor.services.GameConfigValidatorService;
import com.vdha.pokerpredictor.services.SimulatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class PokerPredictorController {

    @Autowired
    private SimulatorService simulatorService;

    @Autowired
    private GameConfigValidatorService gameConfigValidator;

    @ExceptionHandler(ValidatorException.class)
    protected ResponseEntity<Object> handleValidatorException(
            ValidatorException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @GetMapping(path = "/predict")
    ResponseEntity<ProbabilityResults> predict(@RequestBody GameConfig gameConfig) throws ValidatorException {
            gameConfigValidator.validate(gameConfig);
            ProbabilityResults result = simulatorService.predict(gameConfig);
            return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
