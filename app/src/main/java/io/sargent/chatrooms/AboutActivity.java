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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jmk1637 on 10/8/2015.
 */
public class AboutActivity extends AppCompatActivity {

    private EditText CustomColorEditText;
    private EditText CustomNameEditText;
    private CheckBox WantsCustomNameCheckBox;
    private CheckBox WantsCustomColorCheckBox;
    private Toolbar mActionbar;
    private Button settingsButton;
    private Button backButton;
    private Socket mSocket;
    private GlobalState state;
    private Pattern pattern;
    private Matcher matcher;
    private String HEX_PATTERN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_layout);
        Bundle extras = getIntent().getExtras();
        HEX_PATTERN = "^([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";
        pattern = Pattern.compile(HEX_PATTERN);

        state = (GlobalState)getApplicationContext();
        mSocket = state.getSocket();

        mSocket.on("changePreferencesSuccess", ChangeSuccessful);
        mSocket.on("changePreferencesError", ChangeError);

        settingsButton = (Button) findViewById(R.id.settingsButton);
        backButton = (Button)findViewById(R.id.backButton);

        WantsCustomColorCheckBox = (CheckBox) findViewById(R.id.wantCustomColor);
        WantsCustomNameCheckBox = (CheckBox) findViewById(R.id.wantsCustomName);

        CustomColorEditText = (EditText) findViewById(R.id.CustomColorEditText);
        CustomNameEditText = (EditText) findViewById(R.id.CustomNameEditText);

        mActionbar = (Toolbar) findViewById(R.id.tool_bar);

        if (state.getGlobalWantsCustomName()) {
            WantsCustomNameCheckBox.setChecked(true);
            CustomNameEditText.setVisibility(View.VISIBLE);
            CustomNameEditText.setText(state.getGlobalCustomName());
        } else {
            WantsCustomNameCheckBox.setChecked(false);
            CustomNameEditText.setVisibility(View.INVISIBLE);

        }

        if (state.getGlobalWantsGlobalCustomColor()) {
            WantsCustomColorCheckBox.setChecked(true);
            CustomColorEditText.setVisibility(View.VISIBLE);
            CustomColorEditText.setText(state.getGlobalCustomColor());
            //CustomColorEditText.setTextColor(Color.parseColor("#" + state.getGlobalCustomColor()));
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
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appIntent = new Intent(getApplicationContext(), ChatActivity.class);
                startActivity(appIntent);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  //change settings emit from socket
                  if(WantsCustomColorCheckBox.isChecked()){
                      if(CustomColorEditText.getText().toString().length() == 0){
                          Context context = getApplicationContext();
                          int duration = Toast.LENGTH_SHORT;
                          Toast toast = Toast.makeText(context, "Must Enter A Custom Color", duration);
                          toast.show();
                          return;

                      }
                  }
                  if(WantsCustomNameCheckBox.isChecked()){
                      if(CustomNameEditText.getText().toString().length() == 0){
                          Context context = getApplicationContext();
                          int duration = Toast.LENGTH_SHORT;
                          Toast toast = Toast.makeText(context, "Must Enter A Custom Name", duration);
                          toast.show();
                          return;
                      }
                  }
                  boolean wantsCName = WantsCustomNameCheckBox.isChecked();
                  boolean wantsCColor = WantsCustomColorCheckBox.isChecked();
                  String CColor = "";
                  String CName = "";

                  if(wantsCColor == true){
                      if(validateHexCode(CustomColorEditText.getText().toString())){
                          CColor = CustomColorEditText.getText().toString();

                      }
                      else{
                          Context context = getApplicationContext();
                          int duration = Toast.LENGTH_SHORT;
                          Toast toast = Toast.makeText(context, "Must be a hex color. Ex. aabbcc", duration);
                          toast.show();
                          return;
                      }

                  }
                  else{
                      CColor = "none";

                  }
                  if(wantsCName == true){
                      CName = CustomNameEditText.getText().toString();
                  }
                  else{
                      CName = "none";
                  }

                  JSONObject jsonObj = new JSONObject();

                  try {
                      jsonObj.put("wantsCustomName", wantsCName);
                      jsonObj.put("wantsCustomColor", wantsCColor);
                      jsonObj.put("CustomName", CName);
                      jsonObj.put("CustomColor", CColor);


                  } catch (JSONException e) {
                      return;
                  }
                  mSocket.emit("changePreferences", jsonObj);

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
                    JSONObject data = (JSONObject) args[0];
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, "Changes Successfully Made", duration);
                    toast.show();
                    Intent appIntent = new Intent(getApplicationContext(), ChatActivity.class);
                    startActivity(appIntent);
                    try {
                        state.setGlobalCustomName(data.getString("customName"));
                        state.setGlobalWantsCustomName(data.getBoolean("wantsCustomName"));
                        state.setGlobalWantsCustomColor(data.getBoolean("wantsCustomColor"));
                        state.setGlobalCustomColor(data.getString("customColor"));

                    } catch (JSONException e) {
                        Log.i("CHANGE ERROR", e + "");
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
                    JSONObject data = (JSONObject) args[0];
                    try {
                        Log.i("CHANGE ERROR", data.getString("message") + "");
                    }catch (JSONException e){
                        Log.i("CHANGE ERROR", "A Problem");

                    }
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, "An Error Occured.", duration);
                    toast.show();


                }
            });
        }
    };

    private boolean validateHexCode(final String hexColorCode){
        matcher = pattern.matcher(hexColorCode);
        return matcher.matches();
    }


}