package io.sargent.chatrooms;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

/**
 * Created by jmk1637 on 10/8/2015.
 */
public class AboutActivity extends AppCompatActivity {

    private Toolbar mActionbar;
    private TextView mAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_layout);

        //find and get tool bar and text views
        mAbout = (TextView)findViewById(R.id.about_text);
        mActionbar = (Toolbar) findViewById(R.id.tool_bar);
        mActionbar.setTitle("About");
        //set the text
        mAbout.setText("Palaver was created by Jordan Karlsruher and Tyler Sargent 2015");

    }


}