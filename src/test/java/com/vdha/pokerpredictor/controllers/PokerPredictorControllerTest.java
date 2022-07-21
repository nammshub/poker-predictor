package com.vdha.pokerpredictor.controllers;

import com.vdha.pokerpredictor.services.GameConfigValidatorService;
import com.vdha.pokerpredictor.services.SimulatorService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class PokerPredictorControllerTest {

    @Mock
    private SimulatorService simulatorService;

    @Mock
    private GameConfigValidatorService gameConfigValidator;

    @InjectMocks
    private PokerPredictorController controller;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void verify(){
        Mockito.verifyNoMoreInteractions(simulatorService,gameConfigValidator);
    }



}