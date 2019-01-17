package com.example.xxxxx.flashcards;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;

public class EloCalculator {
    public static final int default_elo = 1200;
    private static final int factorK = 80;

    private static double ErwartungswertElo(double first, double second){
        return 1/(1+Math.pow(10, (first - second)/400));
    }

    public static int calculateWinner(int eloWinner, int eloLoser){
        return (int) (eloWinner + factorK * (1 - ErwartungswertElo((double)eloWinner, (double)eloLoser)));
    }

    public static int calculateLoser(int eloLoser, int eloWinner){
        return (int) (eloLoser + factorK * (0 - ErwartungswertElo((double)eloLoser, (double)eloWinner)));
    }

    public static void setEloInToolbar(ActionBar toolbar, Context context){
        if(toolbar != null) {
            Database database = Database.getInstance(context);
            int elo = database.getUserElo();
            toolbar.setTitle("Current Elo: " + Integer.toString(elo));
        }else{
            Log.d("flashcard", "failed to set Elo in Toolbar");
        }
    }
}
