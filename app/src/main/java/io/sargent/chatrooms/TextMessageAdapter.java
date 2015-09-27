package io.sargent.chatrooms;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TextMessageAdapter extends RecyclerView.Adapter<TextMessageAdapter.TextMessageViewHolder>{
    private List<TextMessageInfo> messages;

    public TextMessageAdapter(){
        messages = new ArrayList<TextMessageInfo>();
    }

    public void addMessage(TextMessageInfo m){
        messages.add(m);
    }

    @Override
    public int getItemCount(){
        return messages.size();
    }

    @Override
    public void onBindViewHolder(TextMessageViewHolder textViewHolder, int i){
        TextMessageInfo t = messages.get(i);

        textViewHolder.vMessage.setText(t.getMessage());
        textViewHolder.vUser.setText(t.getUser());
    }

    @Override
    public TextMessageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View itemView = LayoutInflater.
                            from(viewGroup.getContext()).
                            inflate(R.layout.text_message_layout, viewGroup, false);

        return new TextMessageViewHolder(itemView);
    }

    public static class TextMessageViewHolder extends RecyclerView.ViewHolder{
        protected TextView vUser;
        protected TextView vMessage;

        public TextMessageViewHolder(View v){
            super(v);
            vUser = (TextView)v.findViewById(R.id.user);
            vMessage = (TextView)v.findViewById(R.id.message);
        }
    }
}
