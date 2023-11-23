package Chatroom.Data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ChatMessage {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name="messages")
    public String messages;
    @ColumnInfo(name="TimeSent")
    public String timeSent;
    @ColumnInfo(name="SendOrReceive")
    public boolean isSentButton;
    public ChatMessage(){}
    public ChatMessage(String messages, String timeSent, boolean isSentButton) {
        this.messages = messages;
        this.timeSent = timeSent;
        this.isSentButton = isSentButton;
    }

    public String getMessages() {
        return messages;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public boolean isSentButton() {
        return isSentButton;
    }



}