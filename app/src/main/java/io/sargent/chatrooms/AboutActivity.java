package io.sargent.chatrooms;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by jmk1637 on 10/8/2015.
 */
public class AboutActivity extends AppCompatActivity {

    private Toolbar mActionbar;
    private TextView userNameInfo;
    private TextView userColorInfo;
    private Button settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_layout);
        Bundle extras = getIntent().getExtras();
        userNameInfo = (TextView)findViewById(R.id.usernameInfo);
        userColorInfo = (TextView)findViewById(R.id.usercolorInfo);
        settingsButton = (Button)findViewById(R.id.settingsButton);

        mActionbar = (Toolbar) findViewById(R.id.tool_bar);
        mActionbar.setTitle("About");

        if(extras.getBoolean("wantsCustomName")){
            userNameInfo.setText("You are currently using the custom name "+ extras.getString("userName"));
        }
        else{
            userNameInfo.setText("You are currently getting a random name from our servers.");
        }

        if(extras.getBoolean("wantsCustomColor")){
            userColorInfo.setText("You are currently using the custom Color " + extras.getString("userColor"));
            Color c = new Color();
            userColorInfo.setTextColor(Color.parseColor("#"+extras.getString("userColor")));
        }
        else{
            userColorInfo.setText("You are currently getting a color name from our servers.");
        }

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://palaver-server.herokuapp.com"));
                Intent browserChooserIntent = Intent.createChooser(browserIntent , "Choose browser of your choice");
                startActivity(browserChooserIntent );

            }
        });


        //

        //set the text

    }


}