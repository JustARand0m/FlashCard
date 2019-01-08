package com.example.xxxxx.flashcards;

public class FlashCard {
    private String Question;
    private String Answer;
    private int index;

    public FlashCard(String question, String answer, int iIndex){
        setQuesitonAndAnswer(question, answer, iIndex);
    }

    public void setQuesitonAndAnswer(String question, String answer, int iIndex){
        Answer = answer;
        Question = question;
        index = iIndex;
    }

    public int getIndex(){return index;}

    public String getAnswer(){
        return Answer;
    }

    public String getQuestion(){
        return Question;
    }
}
