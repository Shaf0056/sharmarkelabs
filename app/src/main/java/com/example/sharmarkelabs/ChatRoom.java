package com.example.sharmarkelabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import com.example.sharmarkelabs.ui.data.ChatMessage;
import com.example.sharmarkelabs.ui.data.ChatMessageDAO;
import com.example.sharmarkelabs.ui.data.MessageDatabase;
import com.google.android.material.snackbar.Snackbar;


import com.example.sharmarkelabs.databinding.ActivityChatRoomBinding;
import com.example.sharmarkelabs.databinding.ReceiveMessageBinding;
import com.example.sharmarkelabs.databinding.SentMessageBinding;
import com.example.sharmarkelabs.ui.data.ChatRoomViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;




public class ChatRoom extends AppCompatActivity {

    ActivityChatRoomBinding binding;
    ArrayList<ChatMessage> messages = new ArrayList<>();

    ChatRoomViewModel chatModel;




    private RecyclerView.Adapter<MyRowHolder> myAdapter;

    private ChatMessageDAO mDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "database-name").build();
        mDAO = db.cmDAO();

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recycleView.setLayoutManager(layoutManager);

        myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                if (viewType == 0) {
                    SentMessageBinding binding = SentMessageBinding.inflate(inflater, parent, false);
                    return new MyRowHolder(binding.getRoot());
                } else {
                    ReceiveMessageBinding binding = ReceiveMessageBinding.inflate(inflater, parent, false);
                    return new MyRowHolder(binding.getRoot());
                }
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                if (position < messages.size()) {
                    ChatMessage chatMessage = messages.get(position);
                    holder.bind(chatMessage, mDAO);
                }
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }

            @Override
            public int getItemViewType(int position) {
                ChatMessage chatMessage = messages.get(position);
                if (chatMessage.isSentButton()) {
                    return 0;
                } else {
                    return 1;
                }
            }
        };

        binding.recycleView.setAdapter(myAdapter);

        binding.sendButton.setOnClickListener(click -> {
            String message = binding.textInput.getText().toString();
            String timeSent = getCurrentTime();
            boolean isSentButton = true;
            ChatMessage chatMessage = new ChatMessage(message, timeSent, isSentButton);

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                mDAO.insertMessage(chatMessage);

                List<ChatMessage> allMessages = mDAO.getAllMessages();

                runOnUiThread(() -> {
                    messages.clear();
                    messages.addAll(allMessages);
                    myAdapter.notifyDataSetChanged();
                    binding.textInput.setText("");
                    chatModel.messages.postValue(new ArrayList<>(messages));
                });
            });
        });

        binding.receiveButton.setOnClickListener(click -> {
            String message = binding.textInput.getText().toString();
            String timeSent = getCurrentTime();
            boolean isSentButton = false;
            ChatMessage chatMessage = new ChatMessage(message, timeSent, isSentButton);

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                mDAO.insertMessage(chatMessage);

                List<ChatMessage> allMessages = mDAO.getAllMessages();

                runOnUiThread(() -> {
                    messages.clear();
                    messages.addAll(allMessages);
                    myAdapter.notifyDataSetChanged();
                    binding.textInput.setText("");
                    chatModel.messages.postValue(new ArrayList<>(messages));
                });
            });
        });
    }


    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy \nhh:mm:ss a");
        return sdf.format(new Date());
    }


    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);
        }

        public void bind(ChatMessage chatMessage, ChatMessageDAO mDAO) {
            messageText.setText(chatMessage.getMessage());
            timeText.setText(chatMessage.getTimeSent());

            itemView.setOnClickListener(clk -> {
                int position = getAbsoluteAdapterPosition();
                ChatMessage removeMessage = messages.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                builder.setMessage("Do you want to delete this message: " + chatMessage.getMessage());
                builder.setTitle("Question");
                builder.setNegativeButton("No", (dialog, cl) -> {
                });
                builder.setPositiveButton("Yes", (dialog, cl) -> {

                    ChatMessage removedMessage = messages.get(position);
                    messages.remove(position);
                    myAdapter.notifyItemRemoved(position);

                    Executor thread = Executors.newSingleThreadExecutor();
                    thread.execute(() -> {
                        mDAO.deleteMessage(removedMessage);


                        runOnUiThread(() -> {
                            Snackbar.make(itemView, "You deleted message #" + position, Snackbar.LENGTH_LONG)
                                    .setAction("Undo", undoclk -> {
                                        // Undo logic here
                                        messages.add(position, removedMessage);
                                        myAdapter.notifyItemInserted(position);
                                    })
                                    .show();
                        });
                    });
                });
                builder.create().show();
            });
        }
    }
}
