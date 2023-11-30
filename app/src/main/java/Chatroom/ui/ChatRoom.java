package Chatroom.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import Chatroom.Data.ChatMessage;
import Chatroom.Data.ChatMessageDAO;
import Chatroom.Data.ChatViewModel;
import Chatroom.Data.MessageDatabase;
import algonquin.cst2335.shaf0056.R;
import algonquin.cst2335.shaf0056.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.shaf0056.databinding.ReceiveMessageBinding;
import algonquin.cst2335.shaf0056.databinding.SentMessageBinding;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ChatRoom extends AppCompatActivity {

    private RecyclerView.Adapter<MyRowHolder> myAdapter;
    private ArrayList<ChatMessage> messages;
    private ChatMessageDAO mDAO;
    private Executor thread;
    private ActivityChatRoomBinding binding;
    private ChatViewModel chatModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        chatModel = new ViewModelProvider(this).get(ChatViewModel.class);
        messages = chatModel.messages.getValue();
        if (messages == null) {
            messages = new ArrayList<>();
            chatModel.messages.setValue(messages);
            loadMessages();
        }

        setupRecyclerView();
        setupSendReceiveButtons();
    }

    private void loadMessages() {
        thread = Executors.newSingleThreadExecutor();
        thread.execute(() -> {
            MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "database-name").build();
            mDAO = db.cmDAO();
            messages.addAll(mDAO.getAllMessages());
            runOnUiThread(() -> myAdapter.notifyDataSetChanged());
        });
    }

    private void setupRecyclerView() {
        binding.theRecycleView.setLayoutManager(new LinearLayoutManager(this));
        binding.theRecycleView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if (viewType == 0) {
                    SentMessageBinding sentMessageBinding = SentMessageBinding.inflate(getLayoutInflater(), parent, false);
                    return new MyRowHolder(sentMessageBinding.getRoot());
                } else {
                    ReceiveMessageBinding receiveMessagesBinding = ReceiveMessageBinding.inflate(getLayoutInflater(), parent, false);
                    return new MyRowHolder(receiveMessagesBinding.getRoot());
                }
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                String strMessage = messages.get(position).getMessages();
                holder.messageText.setText(strMessage);
                holder.timeText.setText(messages.get(position).getTimeSent());
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }

            @Override
            public int getItemViewType(int position) {
                return messages.get(position).isSentButton() ? 0 : 1;
            }
        });
    }

    private void setupSendReceiveButtons() {
        binding.sendButton.setOnClickListener(click -> sendMessage(true));
        binding.button.setOnClickListener(click -> sendMessage(false));
    }

    private void sendMessage(boolean isSent) {
        String text = binding.textInput.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, hh:mm a");
        String currentTime = sdf.format(new Date());
        ChatMessage newMessage = new ChatMessage(text, currentTime, isSent);
        messages.add(newMessage);
        thread.execute(() -> mDAO.insertMessage(newMessage));
        runOnUiThread(() -> {
            myAdapter.notifyItemInserted(messages.size() - 1);
            binding.textInput.setText("");
            binding.theRecycleView.smoothScrollToPosition(messages.size() - 1);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_delete) {
            showDeleteConfirmationDialog();
            return true;
        } else if (item.getItemId() == R.id.item_about) {
            Toast.makeText(this, "Version 1.0, created by YourName", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteConfirmationDialog() {
        ChatMessage selectedMessage = chatModel.selectedMessage.getValue();
        if (selectedMessage != null) {
            new AlertDialog.Builder(this)
                    .setMessage("Do you want to delete this message?")
                    .setPositiveButton("Delete", (dialog, which) -> deleteMessage(selectedMessage))
                    .setNegativeButton("Cancel", null)
                    .show();
        } else {
            Toast.makeText(this, "No message selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteMessage(ChatMessage message) {
        int messageIndex = -1;
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).id == message.id) {
                messageIndex = i;
                break;
            }
        }

        if (messageIndex != -1) {
            final int finalMessageIndex = messageIndex;
            thread.execute(() -> {
                mDAO.deleteMessage(message);
                runOnUiThread(() -> {
                    messages.remove(finalMessageIndex);
                    myAdapter.notifyItemRemoved(finalMessageIndex);
                });
            });
        } else {
            runOnUiThread(() -> Toast.makeText(ChatRoom.this, "Message not found", Toast.LENGTH_SHORT).show());
        }
    }

    public class MyRowHolder extends RecyclerView.ViewHolder {
        public TextView messageText;
        public TextView timeText;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(clk -> {
                int position = getAbsoluteAdapterPosition();
                ChatMessage selected = messages.get(position);
                ChatViewModel.selectedMessage.postValue(selected);

                // Handle fragment transaction here
                // For example, showing a detail fragment for the selected message
                MessageDetailsFragment chatFragment = new MessageDetailsFragment(selected);
                ((AppCompatActivity) itemView.getContext()).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentLocation, chatFragment) // Replace with your fragment container ID
                        .addToBackStack(null)
                        .commit();
            });

            messageText = itemView.findViewById(R.id.m);
            timeText = itemView.findViewById(R.id.t);
        }
    }
}