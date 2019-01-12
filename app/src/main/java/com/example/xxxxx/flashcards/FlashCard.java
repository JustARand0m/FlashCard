package com.example.xxxxx.flashcards;

public class FlashCard {
    private String Question;
    private int Elo;
    private boolean Solved;
    private String Answer;
    private int index;
    private String questionImage;
    private String answerImage;

    public FlashCard(String question, String answer, int iIndex, int elo, boolean solved , String questionimage, String answerimage){
        setQuesitonAndAnswer(question, answer, iIndex, elo, solved, questionimage, answerimage);
    }

    public void setQuesitonAndAnswer(String question, String answer, int iIndex, int elo, boolean solved, String questionimage, String answerimage){
        questionImage = questionimage;
        answerImage = answerimage;
        Answer = answer;
        Question = question;
        index = iIndex;
        Elo = elo;
        Solved = solved;
    }

    public boolean getSolved(){return Solved;}

    public String getQuestionImage(){
        return questionImage;
    }

    public String getAnswerImage(){
        return answerImage;
    }

    public int getElo(){return Elo;}

    public int getIndex(){return index;}

    public String getAnswer(){
        return Answer;
    }

    public String getQuestion(){
        return Question;
    }
}
