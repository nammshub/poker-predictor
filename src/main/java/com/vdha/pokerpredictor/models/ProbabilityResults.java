package com.vdha.pokerpredictor.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProbabilityResults {
    double preFlopWinRate;
    double flopWinRate;
    double turnWinRate;
    double riverWinRate;
}
