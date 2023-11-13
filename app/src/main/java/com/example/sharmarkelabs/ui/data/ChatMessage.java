package com.example.sharmarkelabs.ui.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ChatMessage {

    @ColumnInfo
    protected String message;
    @ColumnInfo
    protected String timeSent;
    @ColumnInfo
    protected boolean isSentButton;

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name="id")
    public long id;



    public ChatMessage(String m, String t, boolean sent){
        message = m;
        timeSent = t;
        isSentButton = sent;
    }

    public String getMessage(){
        return message;
    }
    public String getTimeSent(){
        return timeSent;
    }
    public boolean isSentButton(){
        return isSentButton;
    }

    public ChatMessage(){}


}