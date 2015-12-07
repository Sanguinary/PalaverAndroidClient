package io.sargent.chatrooms;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class AddRoomActivity extends AppCompatActivity{
    private EditText mRoomEntry;
    private Button mSubmitButton;

    private Context mCtx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        mCtx = this;

        mRoomEntry = (EditText)findViewById(R.id.room_name_entry);
        mSubmitButton = (Button)findViewById(R.id.submit_new_room);


        mSubmitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(mRoomEntry.getText().toString().equals("")){
                    Toast.makeText(mCtx, "Invalid room name", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent i = new Intent();
                i.putExtra("room_name", mRoomEntry.getText().toString());
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }
}
