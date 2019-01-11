package com.example.xxxxx.flashcards;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class FlashcardAdapter extends RecyclerView.Adapter<FlashcardAdapter.MyViewHolder> {
    private LayoutInflater mInflater;
    private ArrayList<FlashCard> flashCards;
    private Database db;
    private Context mContext;
    private int FolderID;

    public FlashcardAdapter(Context context, int Folder){
        db = Database.getInstance(context);
        mInflater = LayoutInflater.from(context);
        FolderID = Folder;
        flashCards = db.getAllFlashCardsOfTable(FolderID);
        mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mItemView = mInflater.inflate(R.layout.flashcarditem, viewGroup, false);
        return new MyViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        flashCards = db.getAllFlashCardsOfTable(FolderID);
        int CardID = flashCards.get(i).getIndex();
        String Question = flashCards.get(i).getQuestion();

        myViewHolder.textNr.setText(Integer.toString(i));
        myViewHolder.textQuestion.setText(Question);
        myViewHolder.CardID = CardID;
    }

    @Override
    public int getItemCount() {
        flashCards = db.getAllFlashCardsOfTable(FolderID);
        return flashCards.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        public TextView textNr;
        public TextView textQuestion;
        public FlashcardAdapter mAdapter;
        public int CardID;

        public MyViewHolder(@NonNull View itemView, FlashcardAdapter adapter) {
            super(itemView);
            textNr = itemView.findViewById(R.id.CountNr);
            textQuestion = itemView.findViewById(R.id.question);
            this.mAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mAdapter.mContext, FlashcardsChangeActivity.class);
            intent.putExtra("LayoutPos", CardID);
            mAdapter.mContext.startActivity(intent);
        }
    }
}
