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

/**
 * Created by Jordan on 11/29/2015.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText mUsernameInput;
    private EditText mPasswordInput;
    private Button mLoginButton;
    private Button mCreateAccountButton;
    private TextView mErrorText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        mUsernameInput = (EditText)findViewById(R.id.usernameinput);
        mPasswordInput = (EditText)findViewById(R.id.passwordinput);

        mErrorText = (TextView)findViewById(R.id.errorText);

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


               }
            }
        });

        mCreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://palaver-server.herokuapp.com/signup"));
                Intent browserChooserIntent = Intent.createChooser(browserIntent , "Choose browser of your choice");
                startActivity(browserChooserIntent );

            }
        });



    }
}
