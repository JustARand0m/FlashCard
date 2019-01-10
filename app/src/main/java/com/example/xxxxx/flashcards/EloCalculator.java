package com.example.xxxxx.flashcards;

public class EloCalculator {
    public static final int default_elo = 1200;
    private static final int factorK = 40;

    private static double ErwartungswertElo(double first, double second){
        return 1/(1+Math.pow(10, (first - second)/400));
    }

    public static int calculateWinner(int eloWinner, int eloLoser){
        return (int) (eloWinner + factorK * (1 - ErwartungswertElo((double)eloWinner, (double)eloLoser)));
    }

    public static int calculateLoser(int eloLoser, int eloWinner){
        return (int) (eloWinner + factorK * (0 - ErwartungswertElo((double)eloLoser, (double)eloWinner)));
    }
}
