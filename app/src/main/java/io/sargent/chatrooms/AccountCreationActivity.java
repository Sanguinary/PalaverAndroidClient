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
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jordan on 12/3/2015.
 */
public class AccountCreationActivity extends AppCompatActivity {
    private static final String TAG = "Account Creation";


    private EditText usernameEditText;
    private EditText pass1EditText;
    private EditText pass2EditText;

    private Socket mSocket;

    private Button createButton;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_creation_layout);

        GlobalState state = (GlobalState)getApplicationContext();
        mSocket = state.getSocket();
        mSocket.on("accountCreationSuccess", AccountCreationSuccess);
        mSocket.on("accountCreationError", AccountCreationError);

        createButton = (Button)findViewById(R.id.creationButton);
        backButton = (Button)findViewById(R.id.creationBackButton);

        usernameEditText = (EditText)findViewById(R.id.usernameCreateTextEdit);
        pass1EditText = (EditText)findViewById(R.id.pass1CreateTextEdit);
        pass2EditText = (EditText)findViewById(R.id.pass2CreateTextEdit);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appIntent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(appIntent);
            }
        });


        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = usernameEditText.getText().toString();
                String pass1 = pass1EditText.getText().toString();
                String pass2 = pass2EditText.getText().toString();
                Log.d(TAG, pass1.length() + " " + pass2.length());


                if (pass1.equals(pass2) == false) {
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, "Passwords do not match", duration);
                    toast.show();
                } else {

                    JSONObject jsonObj = new JSONObject();
                    try {
                        jsonObj.put("username", username);
                        jsonObj.put("pass1", pass1);

                    } catch (JSONException e) {
                        Log.d(TAG, e.getMessage());
                    }

                    mSocket.emit("createAccount", jsonObj);
                }
            }
        });


    }

    private Emitter.Listener AccountCreationError = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, "Account Creation Error", duration);
                    toast.show();

                }
            });
        }
    };

    private Emitter.Listener AccountCreationSuccess = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, "Account Created Successfully", duration);
                    toast.show();
                    Intent appIntent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(appIntent);

                }
            });
        }
    };




}
