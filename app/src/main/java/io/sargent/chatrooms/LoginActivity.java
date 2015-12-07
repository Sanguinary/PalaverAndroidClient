package io.sargent.chatrooms;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

/**
 * Created by Jordan on 11/29/2015.
 */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "Login Activity";


    private EditText mUsernameInput;
    private EditText mPasswordInput;
    private Button mLoginButton;
    private Button mCreateAccountButton;

    private Socket mSocket;

    GlobalState state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        state = (GlobalState)getApplicationContext();
        state.start();
        mSocket = state.getSocket();
        mSocket.on("LoginSuccessful", LoginSuccessful);
        mSocket.on("LoginError", LoginFailed);
        mSocket.connect();



        mUsernameInput = (EditText)findViewById(R.id.usernameinput);
        mPasswordInput = (EditText)findViewById(R.id.passwordinput);


        mLoginButton = (Button)findViewById(R.id.loginbutton);
        mCreateAccountButton = (Button)findViewById(R.id.createbutton);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if(mUsernameInput.getText().toString().length() == 0 || mPasswordInput.getText().toString().length() == 0){
                   Context context = getApplicationContext();
                   CharSequence text = "Must enter all fields";
                   int duration = Toast.LENGTH_SHORT;

                   Toast toast = Toast.makeText(context, text, duration);
                   toast.show();
               }
                else{
                   //code here for logging in to server
                   String username = mUsernameInput.getText().toString();
                   String password = mPasswordInput.getText().toString();
                   JSONObject jsonObj = new JSONObject();
                   try{
                       jsonObj.put("username", username);
                       jsonObj.put("password", password);

                   } catch(JSONException e){
                       Log.d(TAG, e.getMessage());
                   }

                   mSocket.emit("login", jsonObj);

                   mPasswordInput.setText("");


               }
            }
        });

        mCreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appIntent = new Intent(getApplicationContext(), AccountCreationActivity.class);
                startActivity(appIntent);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

            mSocket.connect();


    }

    private Emitter.Listener LoginSuccessful = new Emitter.Listener() {
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
                        state.setGlobalCustomName(data.getString("CustomName"));
                        state.setGlobalWantsCustomName(data.getBoolean("wantsCustomName"));
                        state.setGlobalWantsCustomColor(data.getBoolean("wantsCustomColor"));
                        state.setGlobalCustomColor(data.getString("CustomColor"));


                        startActivity(appIntent);
                    } catch (JSONException e) {
                        return;
                    }


                }
            });
        }
    };


    private Emitter.Listener LoginFailed = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String text;
                    try {
                        text = data.getString("message");


                    } catch (JSONException e) {
                        Log.d(TAG, e.getMessage());
                        return;
                    }

                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            });
        }
    };
}
