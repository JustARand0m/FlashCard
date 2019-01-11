package com.example.xxxxx.flashcards;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MainFolderAdapter extends RecyclerView.Adapter<MainFolderAdapter.MyViewHolder>{
    private LayoutInflater mInflater;
    private ArrayList<String> Folders;
    private ArrayList<Integer> Keys;
    private Database db;
    private Context mContext;


    public MainFolderAdapter(Context context){
        db = Database.getInstance(context);
        Keys = db.getAllFolderPrimary();
        Folders = db.getAllFolders();
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mItemView = mInflater.inflate(R.layout.folderitem, viewGroup, false);
        return new MyViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Folders = db.getAllFolders();
        Keys = db.getAllFolderPrimary();

        //set the foldername and the Primary key for each ViewHolder (Key is for Viewholder to pass it on Click to ChangeFlashCardActivity)
        myViewHolder.textFolderName.setText(Folders.get(i));
        myViewHolder.PrimaryKey = Keys.get(i);
    }

    @Override
    public int getItemCount() {
        Folders = db.getAllFolders();
        return Folders.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener, View.OnLongClickListener{
        public int PrimaryKey;
        public TextView textFolderName;
        public MainFolderAdapter mAdapter;

        public MyViewHolder(@NonNull View itemView, MainFolderAdapter adapter) {
            super(itemView);
            textFolderName = itemView.findViewById(R.id.folder_name);
            this.mAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mAdapter.mContext, FlashcardsActivity.class);
            intent.putExtra("pos", PrimaryKey);
            mAdapter.mContext.startActivity(intent);
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }
}
