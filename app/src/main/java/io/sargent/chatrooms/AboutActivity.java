package io.sargent.chatrooms;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jmk1637 on 10/8/2015.
 */
public class AboutActivity extends AppCompatActivity {

    private Toolbar mActionbar;
    private EditText CustomColorEditText;
    private EditText CustomNameEditText;
    private CheckBox WantsCustomNameCheckBox;
    private CheckBox WantsCustomColorCheckBox;
    private Button settingsButton;
    private Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_layout);
        Bundle extras = getIntent().getExtras();

        GlobalState state = (GlobalState)getApplicationContext();
        mSocket = state.getSocket();

        mSocket.on("changePreferencesSuccess", ChangeSuccessful);
        mSocket.on("changePreferencesError", ChangeError);

        settingsButton = (Button) findViewById(R.id.settingsButton);

        WantsCustomColorCheckBox = (CheckBox) findViewById(R.id.wantCustomColor);
        WantsCustomNameCheckBox = (CheckBox) findViewById(R.id.wantsCustomName);

        CustomColorEditText = (EditText) findViewById(R.id.CustomColorEditText);
        CustomNameEditText = (EditText) findViewById(R.id.CustomNameEditText);

        mActionbar = (Toolbar) findViewById(R.id.tool_bar);
        mActionbar.setTitle("About");

        if (extras.getBoolean("wantsCustomName")) {
            WantsCustomNameCheckBox.setChecked(true);
            CustomNameEditText.setVisibility(View.VISIBLE);
            CustomNameEditText.setText(extras.getString("userName"));
        } else {
            WantsCustomNameCheckBox.setChecked(false);
            CustomNameEditText.setVisibility(View.INVISIBLE);

        }

        if (extras.getBoolean("wantsCustomColor")) {
            WantsCustomColorCheckBox.setChecked(true);
            CustomColorEditText.setVisibility(View.VISIBLE);
            CustomColorEditText.setText(extras.getString("userColor"));
            CustomColorEditText.setTextColor(Color.parseColor("#" + extras.getString("userColor")));
        } else {
            WantsCustomColorCheckBox.setChecked(false);
            CustomColorEditText.setVisibility(View.INVISIBLE);
        }

        WantsCustomColorCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    CustomColorEditText.setVisibility(View.VISIBLE);
                } else {
                    CustomColorEditText.setVisibility(View.INVISIBLE);

                }

            }
        });

        WantsCustomNameCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    CustomNameEditText.setVisibility(View.VISIBLE);
                } else {
                    CustomNameEditText.setVisibility(View.INVISIBLE);

                }

            }
        });

        CustomColorEditText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener()

          {
              @Override
              public void onClick(View v) {
                  //change settings emit from socket
                  mSocket.emit("changePreferences");

              }
          }

        );


    }

    private Emitter.Listener ChangeSuccessful = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String username;
                    String color;
                    Log.i("LOGINACTIVITY", "LOGIN SUCCESSFUL");
                    JSONObject data = (JSONObject) args[0];
                    String text = "Login Successful";
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                    Intent appIntent = new Intent(getApplicationContext(), ChatActivity.class);
                    Bundle b = new Bundle();
                    try {
                        appIntent.putExtra("wantsCustomColor", data.getBoolean("wantsCustomColor"));
                        appIntent.putExtra("wantsCustomName", data.getBoolean("wantsCustomName"));
                        if(data.getBoolean("wantsCustomColor")){
                            color = data.getString("CustomColor");
                            appIntent.putExtra("CustomColor", color);

                        }
                        if(data.getBoolean("wantsCustomName")){
                            username = data.getString("CustomName");
                            appIntent.putExtra("CustomName", username);

                        }
                        startActivity(appIntent);
                    } catch (JSONException e) {
                        return;
                    }


                }
            });
        }
    };

    private Emitter.Listener ChangeError = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String username;
                    String color;
                    Log.i("LOGINACTIVITY", "LOGIN SUCCESSFUL");
                    JSONObject data = (JSONObject) args[0];
                    String text = "Login Successful";
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                    Intent appIntent = new Intent(getApplicationContext(), ChatActivity.class);
                    Bundle b = new Bundle();
                    try {
                        appIntent.putExtra("wantsCustomColor", data.getBoolean("wantsCustomColor"));
                        appIntent.putExtra("wantsCustomName", data.getBoolean("wantsCustomName"));
                        if(data.getBoolean("wantsCustomColor")){
                            color = data.getString("CustomColor");
                            appIntent.putExtra("CustomColor", color);

                        }
                        if(data.getBoolean("wantsCustomName")){
                            username = data.getString("CustomName");
                            appIntent.putExtra("CustomName", username);

                        }
                        startActivity(appIntent);
                    } catch (JSONException e) {
                        return;
                    }


                }
            });
        }
    };


}