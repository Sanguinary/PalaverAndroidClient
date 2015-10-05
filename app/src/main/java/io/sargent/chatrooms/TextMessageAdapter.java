package io.sargent.chatrooms;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TextMessageAdapter extends RecyclerView.Adapter<TextMessageAdapter.TextMessageViewHolder>{
    private List<TextMessageInfo> messages;
    private Context context;

    public TextMessageAdapter(Context ctx){
        context = ctx;
        messages = new ArrayList<TextMessageInfo>();
    }

    public void addMessage(TextMessageInfo m){
        messages.add(m);
    }

    public void clearMessages(){
        messages.clear();
    }

    @Override
    public int getItemCount(){
        return messages.size();
    }

    @Override
    public void onBindViewHolder(TextMessageViewHolder textViewHolder, int i){
        TextMessageInfo t = messages.get(i);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)textViewHolder.vTextMessage.getLayoutParams();
        RelativeLayout.LayoutParams subParams = (RelativeLayout.LayoutParams)textViewHolder.vUserContainer.getLayoutParams();

        textViewHolder.vMessage.setText(t.getMessage());
        textViewHolder.vUser.setText(t.getUser());

        if(t.isSender()){
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);

            GradientDrawable shape = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[] {0xFFEEEEEE, 0xFFEEEEEE});
            float den = context.getResources().getDisplayMetrics().density;
            shape.setCornerRadii(new float[] {
                    den * 8f, den * 8f, // top-left
                    den * 0f, den * 0f, // top-right
                    den * 8f, den * 8f, // bottom-right
                    den * 8f, den * 8f  // bottom-left
            });
            textViewHolder.vTextMessage.setBackground(shape);

            TextView tv = (TextView)textViewHolder.vTextMessage.findViewById(R.id.message);
            tv.setTextColor(context.getResources().getColor(R.color.ColorPrimaryTextDark));

            subParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            subParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);

        } else {
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);

            GradientDrawable shape = new GradientDrawable(
                    GradientDrawable.Orientation.BOTTOM_TOP, new int[] {    context.getResources().getColor(R.color.ColorPrimaryLight),
                                                                            context.getResources().getColor(R.color.ColorPrimaryLight)});
            float den = context.getResources().getDisplayMetrics().density;
            shape.setCornerRadii(new float[]{
                    den * 0f, den * 0f, // top-left
                    den * 8f, den * 8f, // top-right
                    den * 8f, den * 8f, // bottom-right
                    den * 8f, den * 8f  // bottom-left
            });
            textViewHolder.vTextMessage.setBackground(shape);

            TextView tv = (TextView)textViewHolder.vTextMessage.findViewById(R.id.message);
            tv.setTextColor(context.getResources().getColor(R.color.ColorPrimaryTextLight));

            subParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            subParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        }


        textViewHolder.vUserContainer.setLayoutParams(subParams);
        textViewHolder.vTextMessage.setLayoutParams(params);
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
        protected RelativeLayout vUserContainer;
        protected TextView vMessage;
        protected RelativeLayout vTextMessage;
        protected boolean vSender;

        public TextMessageViewHolder(View v){
            super(v);
            vUser = (TextView)v.findViewById(R.id.user);
            vMessage = (TextView)v.findViewById(R.id.message);
            vTextMessage = (RelativeLayout)v.findViewById(R.id.text_layout_view);
            vUserContainer = (RelativeLayout)v.findViewById(R.id.user_container);
        }
    }
}
