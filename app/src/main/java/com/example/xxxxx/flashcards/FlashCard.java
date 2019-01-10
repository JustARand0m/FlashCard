package com.example.xxxxx.flashcards;

public class FlashCard {
    private String Question;
    private int Elo;
    private boolean Solved;
    private String Answer;
    private int index;

    public FlashCard(String question, String answer, int iIndex, int elo, boolean solved){
        setQuesitonAndAnswer(question, answer, iIndex, elo, solved);
    }

    public void setQuesitonAndAnswer(String question, String answer, int iIndex, int elo, boolean solved){
        Answer = answer;
        Question = question;
        index = iIndex;
        Elo = elo;
        Solved = solved;
    }

    public boolean getSolved(){return Solved;}

    public int getElo(){return Elo;}

    public int getIndex(){return index;}

    public String getAnswer(){
        return Answer;
    }

    public String getQuestion(){
        return Question;
    }
}
