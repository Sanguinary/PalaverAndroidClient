package io.sargent.chatrooms;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

/**
 * Created by jordan on 11/30/15.
 */
public class GlobalState extends Application {
    private static final String TAG = "Global State";
    private boolean GlobalWantsCustomName;
    private String GlobalCustomName;

    private boolean GlobalWantsCustomColor;
    private String GlobalCustomColor;

    private Socket mSocket;

    public void start()
    {
        GlobalCustomName = "";
        GlobalWantsCustomName = false;
        GlobalCustomColor = "";
        GlobalWantsCustomColor = false;
        //try to connect to the server...
        try{

            mSocket = IO.socket("https://palaver-server.herokuapp.com/");
            //mSocket = IO.socket("http://129.21.115.139:3000/");
            Log.d(TAG, "Connected");
        } catch (URISyntaxException e){
            Toast.makeText(this, R.string.connection_error, Toast.LENGTH_SHORT);
            Log.d(TAG, "Error: Unable to connect to IP. " + e.getMessage());
        }
    }

    public Socket getSocket(){
        return mSocket;
    }
    //gets and sets
    //custom name
    public String getGlobalCustomName(){return GlobalCustomName;}
    public void setGlobalCustomName(String name){ GlobalCustomName = name;}
    //wants name
    public boolean getGlobalWantsCustomName(){return GlobalWantsCustomName;}
    public void setGlobalWantsCustomName(boolean want){ GlobalWantsCustomName = want;}
    //custom color
    public String getGlobalCustomColor(){return GlobalCustomColor;}
    public void setGlobalCustomColor(String color){GlobalCustomColor = color;}
    //wants color
    public boolean getGlobalWantsGlobalCustomColor(){return GlobalWantsCustomColor;}
    public void setGlobalWantsCustomColor(boolean want){GlobalWantsCustomColor = want;}


}
