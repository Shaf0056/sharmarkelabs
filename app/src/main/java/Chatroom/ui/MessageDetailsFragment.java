package Chatroom.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import Chatroom.Data.ChatMessage;
import algonquin.cst2335.shaf0056.databinding.DetailsLayoutBinding;


public class MessageDetailsFragment extends Fragment {

    ChatMessage selected;

    public  MessageDetailsFragment(ChatMessage m){

        selected=m;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


        DetailsLayoutBinding binding=DetailsLayoutBinding.inflate(inflater);


        binding.getRoot().setBackgroundColor(Color.BLUE);
        binding.detailsMessage.setText("Message Content : "+selected.getMessages());
        binding.detailsTime.setText("Time : "+selected.getTimeSent());
        binding.detailsSendReceive.setText("Is this message a sent item : " +String.valueOf(selected.isSentButton()));
        binding.detailsDatabaseId.setText("Id = "+selected.id);

        return binding.getRoot();

    }
}
