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

    private Socket mSocket;
    {
        //try to connect to the server...
        try{
            mSocket = IO.socket("https://palaver-server.herokuapp.com/");
            Log.d(TAG, "Connected");
        } catch (URISyntaxException e){
            Toast.makeText(this, R.string.connection_error, Toast.LENGTH_SHORT);
            Log.d(TAG, "Error: Unable to connect to IP. " + e.getMessage());
        }
    }

    public Socket getSocket(){
        return mSocket;
    }
}
