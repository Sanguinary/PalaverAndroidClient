package io.sargent.chatrooms;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.github.nkzawa.socketio.client.IO;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ChatActivity extends AppCompatActivity {
    private static final String TAG = "ChatActivity";

    private Socket mSocket;
    {
        try{
            mSocket = IO.socket("https://palaver-server.herokuapp.com/");
            Log.d(TAG, "Connected");
        } catch (URISyntaxException e){
            Toast.makeText(this, R.string.connection_error, Toast.LENGTH_SHORT);
            Log.d(TAG, "Error: Unable to connect to IP. " + e.getMessage());
        }
    }

    private Toolbar mActionbar;

    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;

    private RecyclerView mMessageList;
    private LinearLayoutManager mMessagesLayoutManager;
    private TextMessageAdapter mTextAdapter;

    private ImageButton mSendMessageButton;
    private EditText mMessageText;

    private String userName = "default string";
    private String userColour = "#FFFFFF";

    private Context mCtx = this;

    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Server socket
        mSocket.on(Socket.EVENT_ERROR, onError);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on(Socket.EVENT_RECONNECT_ATTEMPT, onReconnectAttempt);
        mSocket.on("receiveUserMetadata", onConnectMetadata);
        mSocket.on("message", onMessageRecieved);;
        mSocket.connect();

        mActionbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mActionbar);

        mMessageText = (EditText)findViewById(R.id.message_text);

        mMessageList = (RecyclerView)findViewById(R.id.message_view);
        mMessagesLayoutManager = new LinearLayoutManager(this);
        mMessagesLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mMessageList.setLayoutManager(mMessagesLayoutManager);

        mTextAdapter = new TextMessageAdapter(getApplicationContext());
        mMessageList.setAdapter(mTextAdapter);

        mDrawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, mActionbar, R.string.open_drawer, R.string.close_drawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }
        };

        mSendMessageButton = (ImageButton)findViewById(R.id.send_message_text);
        mSendMessageButton.setColorFilter(Color.YELLOW);
        mSendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String m = mMessageText.getText().toString();

                if (TextUtils.isEmpty(m)) {
                    return;
                } else {
                    attemptSendMessage(m);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {

        Log.d(TAG, "finish()");
        mSocket.emit("disconnect", "{}");
        mSocket.disconnect();
        mSocket.off(Socket.EVENT_ERROR, onError);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.off(Socket.EVENT_RECONNECT_ATTEMPT, onReconnectAttempt);
        mSocket.off("message", onMessageRecieved);

        super.finish();
    }

    @Override
    public void onDestroy() {

        Log.d(TAG, "onDestroy()");
        mSocket.emit("disconnect", "{}");
        mSocket.disconnect();
        mSocket.off(Socket.EVENT_ERROR, onError);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.off(Socket.EVENT_RECONNECT_ATTEMPT, onReconnectAttempt);
        mSocket.off("message", onMessageRecieved);

        super.onDestroy();
    }

    private void addMessageToView(TextMessageInfo m){
        // Create timestamp
        calendar = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("dd-MMM-yyyy | hh:mm a");
        String formatted = date.format(calendar.getTime());

        m.user += " | " + formatted;

        mTextAdapter.addMessage(m);
        mTextAdapter.notifyDataSetChanged();
        mMessageList.scrollToPosition(mTextAdapter.getItemCount() - 1);
    }

    private void attemptSendMessage(String msg){

        JSONObject jsonObj = new JSONObject();
        try{
            jsonObj.put("username", userName);
            jsonObj.put("message", msg);
        } catch(JSONException e){
            Log.d(TAG, e.getMessage());
        }

        mMessageText.setText("");

        if(!mSocket.connected()){
            Toast.makeText(this, "Error: Not connected", Toast.LENGTH_SHORT).show();
            return;
        }

        TextMessageInfo m = new TextMessageInfo(userName, msg, true);
        addMessageToView(m);

        hide_keyboard(this);
        mSocket.emit("messageAll", jsonObj);
    }

    private Emitter.Listener onConnectMetadata = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];

                    try {
                        userName = data.getString("username");
                        userColour = data.getString("usercolor");
                    } catch (JSONException e) {
                        return;
                    }
                }
            });
        }
    };

    private Emitter.Listener onMessageRecieved = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    String message;
                    try {
                        username = data.getString("username");
                        message = data.getString("message");
                    } catch (JSONException e) {
                        return;
                    }

                    // add the message to view
                    TextMessageInfo m = new TextMessageInfo(username, message, false);
                    addMessageToView(m);
                }
            });
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText( mCtx,
                                    R.string.disconnection_toast,
                                    Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    private Emitter.Listener onError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "Connection error");
                }
            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
        }
    };

    private Emitter.Listener onReconnectAttempt = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Log.d(TAG, "Reconnect attempt");
                    Toast.makeText( getApplicationContext(),
                                    R.string.reconnection_toast,
                                    Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    public static void hide_keyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if(view == null) {
            view = new View(activity);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
