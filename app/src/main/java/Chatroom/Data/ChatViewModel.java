package Chatroom.Data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import Chatroom.Data.ChatMessage;

public class ChatViewModel extends ViewModel {
    public MutableLiveData<ArrayList<ChatMessage>> messages = new MutableLiveData< >();

    public static MutableLiveData<ChatMessage> selectedMessage = new MutableLiveData<>();

}