package com.example.xxxxx.flashcards;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {

    private static Database dInstance;

    private static final String DATABASE_NAME = "database_QandA";
    private static final int DATABASE_VERSION = 1;

    private final String AnswerTable = "Answer";
    private final String AnswerPrimary = "AnswerID";
    private final String AnswerText = "AnswerText";
    private final String AnswerFolder = "AnswerFolder";
    private final String QuestionPrimaty = "QuestionID";
    private final String QuestionText =  "QuestionText";
    private final String QuestionTable = "Question";
    private final String QuestionFolder = "QuestionFolder";
    private final String FolderTable = "Folder";
    private final String FolderPrimary = "FolderID";
    private final String Elo = "Elo";
    private final String Solved = "Solved";
    private final String FolderName = "FolderName";

    public static synchronized Database getInstance(Context context){
        if(dInstance == null){
            dInstance = new Database(context.getApplicationContext());
        }
        return dInstance;
    }

    private Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createFolder = "CREATE TABLE " + FolderTable + " (" + FolderPrimary + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FolderName + " VARCHAR(255));";
        String createQuestion = "CREATE TABLE " + QuestionTable + " (" + QuestionPrimaty + " INTEGER PRIMARY KEY AUTOINCREMENT, " + QuestionText + " TEXT, "
                + Elo + " INTEGER, " + Solved + " INTEGER, " + QuestionFolder + " INTEGER);";
        String createAnswer = "CREATE TABLE " + AnswerTable + " (" + AnswerPrimary + " INTEGER PRIMARY KEY AUTOINCREMENT, " + AnswerText + " TEXT, " + AnswerFolder + " INTEGER);";
        db.beginTransaction();
        try{
            db.execSQL(createFolder);
            db.execSQL(createAnswer);
            db.execSQL(createQuestion);
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropQuestion = "DROP TABLE IF EXISTS " + QuestionTable;
        String dropAnswer = "DROP TABLE IF EXISTS " + AnswerTable;
        String dropFolder = "DROP TABLE IF EXISTS " + FolderTable;
        db.beginTransaction();
        try{
            db.execSQL(dropFolder);
            db.execSQL(dropAnswer);
            db.execSQL(dropQuestion);
        }finally {
            db.endTransaction();
        }
        onCreate(db);
    }

    public FlashCard getRandomQuestion(int FolderID){
        SQLiteDatabase db = this.getWritableDatabase();

        String Row = null;
        String question = null;
        String answer = null;
        int Elo = EloCalculator.default_elo;
        boolean Solved = false;

        Cursor cursorQuestion = db.rawQuery("SELECT * FROM " + QuestionTable + " WHERE " + FolderID + " = " + QuestionFolder + " AND " + Solved + " = 0 " + " ORDER BY RANDOM() LIMIT 1;", null);
        while(cursorQuestion.moveToNext()){
            Row = cursorQuestion.getString(0);
            question = cursorQuestion.getString(1);
            Elo = cursorQuestion.getInt(2);
            Solved = (0 != cursorQuestion.getInt(3));
        }

        Cursor cursorAnswer = db.rawQuery("SELECT * FROM " + AnswerTable + " WHERE " + AnswerPrimary +" = " + Row, null);
        while(cursorAnswer.moveToNext()){
            answer = cursorAnswer.getString(1);
        }

        db.close();

        if(question == null){
            question = " ";
        }
        if(answer == null){
            answer = " ";
        }
        if(Row == null){
            Row = "0";
        }

        return new FlashCard(question,  answer, Integer.valueOf(Row), Elo, Solved);
    }

    public void changeSolved(int primary, boolean solved){
        SQLiteDatabase db = this.getWritableDatabase();

        int  iSolved = solved ? 1 : 0;
        String update = "UPDATE " + QuestionTable + " SET " + Solved + " = " + iSolved + " WHERE " + QuestionPrimaty + " = " + primary;

        db.execSQL(update);
        db.close();
    }

    public void ChangeElo(int eloWinner, int eloLoser, int primaryWinner, int primaryLoser){
        SQLiteDatabase db = this.getWritableDatabase();

        int winnerElo = EloCalculator.default_elo;
        int loserElo = EloCalculator.default_elo;

        String getEloWinner = "SELECT * FROM " + QuestionTable + " WHERE " + primaryWinner + " = " + QuestionPrimaty;
        String getEloLoser = "SELECT * FROM " + QuestionTable + " WHERE " + primaryLoser + " = " + QuestionPrimaty;

        Cursor winnerCurs = db.rawQuery(getEloWinner, null);
        Cursor loserCurs = db.rawQuery(getEloLoser, null);
        if(winnerCurs.moveToFirst()){
            winnerElo =  winnerCurs.getInt(2);
        }
        if(loserCurs.moveToFirst()){
            loserElo = winnerCurs.getInt(2);
        }

        winnerElo = EloCalculator.calculateWinner(winnerElo, loserElo);
        loserElo = EloCalculator.calculateLoser(loserElo, winnerElo);

        String updateWinner = "UPDATE " + QuestionTable + " SET " + Elo + " = " + winnerElo + " WHERE " + QuestionPrimaty + " = " + primaryWinner;
        String updateLoser = "UPDATE " + QuestionTable + " SET " + Elo + " = " + loserElo + " WHERE " + QuestionPrimaty + " = " + primaryLoser;
        db.execSQL(updateWinner);
        db.execSQL(updateLoser);

        db.close();
    }

    public ArrayList<FlashCard> getAllFlashCardsOfTable(int folderId){
        SQLiteDatabase db = this.getWritableDatabase();

        ArrayList<FlashCard> flashcards = new ArrayList<>();

        String Row = null;
        String question = null;
        String answer = null;
        int Elo = EloCalculator.default_elo;
        boolean Solved = false;

        Cursor cursorQuestion = db.rawQuery("SELECT * FROM " + QuestionTable + " WHERE " + QuestionFolder + " = " + folderId, null);
        while(cursorQuestion.moveToNext()){
            Row = cursorQuestion.getString(0);
            question = cursorQuestion.getString(1);
            Elo = cursorQuestion.getInt(2);
            Solved = (0 != cursorQuestion.getInt(3));

            Cursor cursorAnswer = db.rawQuery("SELECT * FROM " + AnswerTable + " WHERE " + AnswerPrimary +" = " + Row, null);
            while(cursorAnswer.moveToNext()){
                answer = cursorAnswer.getString(1);
            }


            if(question == null){
                question = " ";
            }
            if(answer == null){
                answer = " ";
            }
            if(Row == null){
                Row = "0";
            }

            FlashCard fl = new FlashCard(question, answer, Integer.valueOf(Row), Elo, Solved);
            flashcards.add(fl);
        }

        db.close();
        return flashcards;
    }

    public void addFolder(String folderName){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FolderName, folderName);
        db.insert(FolderTable, null, values);

        db.close();
    }

    public void addQuestionAndAnswer(String question, String answer, int currentFolder){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(QuestionText, question);
        values.put(Elo, EloCalculator.default_elo);
        values.put(Solved, 0);
        values.put(QuestionFolder, currentFolder);
        db.insert(QuestionTable, null, values);
        ContentValues values2 = new ContentValues();
        values2.put(AnswerText, answer);
        values2.put(AnswerFolder, currentFolder);
        db.insert(AnswerTable, null, values2);

        db.close();
    }

    public void ChangeQuestion(int primary, String newQuestion){
        SQLiteDatabase db = this.getWritableDatabase();

        String update = "UPDATE " + QuestionTable + " SET " + QuestionText + " = '" + newQuestion + "' WHERE " + QuestionPrimaty + " = " + primary;
        db.execSQL(update);

        db.close();
    }

    public void ChangeAnswer(int primary, String newAnswer){
        SQLiteDatabase db = this.getWritableDatabase();

        String update = "UPDATE " + AnswerTable + " SET " + AnswerText + " = '" + newAnswer + "' WHERE " + AnswerPrimary + " = " + primary;
        db.execSQL(update);

        db.close();
    }

    public boolean deleteFlashcard(int PrimatyKey){
        SQLiteDatabase db = this.getWritableDatabase();
        boolean bQuestion = db.delete(QuestionTable, QuestionPrimaty + " = " + Integer.toString(PrimatyKey), null) > 0;
        boolean bAnswer = db.delete(AnswerTable, AnswerPrimary + " = " + Integer.toString(PrimatyKey), null) > 0;
        if(!bQuestion && !bAnswer){
            db.close();
           return false;
        }
        db.close();
        return true;
    }

    public FlashCard getFlashcard(int PrimaryKey){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursorQuestion = db.rawQuery("SELECT * FROM " + QuestionTable + " WHERE " + QuestionPrimaty + " = " + PrimaryKey, null);
        Cursor cursorAnswer = db.rawQuery("SELECT * FROM " + AnswerTable + " WHERE " + AnswerPrimary + " = " + PrimaryKey, null);
        String question = null;
        String answer = null;
        int Elo = 1200;
        boolean Solved = false;

        while(cursorAnswer.moveToNext()){
            answer = cursorAnswer.getString(1);
        }

        while(cursorQuestion.moveToNext()){
            question = cursorQuestion.getString(1);
            Elo = cursorQuestion.getInt(2);
            Solved = (0 != cursorQuestion.getInt(3));
        }

        db.close();
        return new FlashCard(question, answer, PrimaryKey, Elo, Solved);
    }

    public ArrayList<Integer> getAllFolderPrimary(){
        SQLiteDatabase db = this.getWritableDatabase();

        ArrayList<Integer> keys = new ArrayList<>();

        Cursor cursorKey = db.rawQuery("SELECT " + FolderPrimary + " FROM " + FolderTable, null);
        while(cursorKey.moveToNext()){
            keys.add(cursorKey.getInt(0));
        }
        db.close();
        return keys;
    }

    public ArrayList<String> getAllFolders(){
        SQLiteDatabase db = this.getWritableDatabase();

        ArrayList<String> Folders = new ArrayList<>();

        Cursor cursorFolder = db.rawQuery("SELECT " + FolderName + " FROM " + FolderTable, null);
        while(cursorFolder.moveToNext()){
            Folders.add(cursorFolder.getString(0));
        }

        db.close();
        return Folders;
    }

}
