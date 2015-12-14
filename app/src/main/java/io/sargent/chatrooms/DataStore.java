package io.sargent.chatrooms;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class DataStore {
    //used to store and load json objects from storage
    //-----Static attributes-----
    private static final String FILE_SUFFIX = "_Message_Store";
    private static final String PREFS_NAME = "9mB26hf_DATA_STORE_PREFERENCES";
    private static final String KEY_NUM_TIMES_RUN = "9mB26hf_NUM_TIMES_RUN";

    private static DataStore sDataStore;


    // Accessor
    public static DataStore get(Context ctx){
        if( sDataStore == null){
            sDataStore = new DataStore(ctx);
        }
        return sDataStore;
    }
    //--------------------------

    // Instance attributes
    private int mNumTimesRun;
    private Context mCtx;

    // Private constructor to setup storage
    private DataStore(Context ctx){
        mCtx = ctx;
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        mNumTimesRun = settings.getInt(KEY_NUM_TIMES_RUN, 0); // Get number of times run, 0 if not found

        //Gson gson = new Gson();
        //String arrayListJSON = settings.getString(KEY_ITEMS_STRING, "[{text:\"Use this app everyday!\",formattedDate:\"1/1/1900\", completed:true}]");

        //mData = gson.fromJson(arrayListJSON, new TypeToken<ArrayList<TodoItem>>(){}.getType()); // Get items, default string if not found

        mNumTimesRun++;
    }

    public JSONObject getJSONObjectFromStorage(String fileName){
        String filePath;

        filePath = mCtx.getFilesDir() + File.separator + fileName;

        File file = new File(filePath);
        String line = "";
        StringBuilder sb = new StringBuilder();

        if(file.exists()){
            try {
                FileInputStream fis = mCtx.openFileInput(file.getName());
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);

                while((line = br.readLine()) != null){
                    sb.append(line);
                }

                br.close();
                isr.close();
                fis.close();
            } catch(Exception e){
                Log.d("ChatActivity", "getJSONObjectFromStorage(): " + e.getMessage());
            }
        } else {
            try {
                FileOutputStream fos = mCtx.openFileOutput(file.getName(), Context.MODE_PRIVATE);
                fos.close();
            } catch( Exception e){
                Log.d("ChatActivity", "getJSONObjectFromStorage(): " + e.getMessage());
            }
        }

        String json = sb.toString();
        JSONObject jsonObj = new JSONObject();

        try{
            jsonObj = new JSONObject(json);
        } catch(Exception e){
            Log.d("ChatActivity", "getJSONObjectFromStorage(): " + e.getMessage());
        }

        return jsonObj;
    }

    public void setJSONObjectInStorage(String fileName, JSONObject obj){
        String filePath;

        filePath = mCtx.getFilesDir() + File.separator + fileName;

        File file = new File(filePath);

        try {
            FileOutputStream fos = mCtx.openFileOutput(file.getName(), Context.MODE_PRIVATE);

            fos.write(obj.toString().getBytes());

            fos.close();
        } catch(Exception e){
            Log.d("ChatActivity", "setJSONObjectInStorage(): " + e.getMessage());
        }
    }

    public JSONArray getJSONArrayFromStorage(String fileName, Boolean isMessages){
        String filePath;

        if(isMessages){
            filePath = mCtx.getFilesDir() + File.separator + fileName + FILE_SUFFIX;
        } else {
            filePath = mCtx.getFilesDir() + File.separator + fileName;
        }


        File file = new File(filePath);
        String line = "";
        StringBuilder sb = new StringBuilder();

        if(file.exists()){
            try {
                FileInputStream fis = mCtx.openFileInput(file.getName());
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);

                while((line = br.readLine()) != null){
                    sb.append(line);
                }

                br.close();
                isr.close();
                fis.close();
            } catch(Exception e){
                Log.d("ChatActivity","getJSONArrayFromStorage(): " + e.getMessage());
            }
        } else {
            try {
                FileOutputStream fos = mCtx.openFileOutput(file.getName(), Context.MODE_PRIVATE);
                fos.close();
            } catch( Exception e){
                Log.d("ChatActivity", "getJSONArrayFromStorage(): " + e.getMessage());
            }
        }

        String json = sb.toString();
        JSONArray jsonArray = new JSONArray();

        try{
            jsonArray = new JSONArray(json);
        } catch(Exception e){
            Log.d("ChatActivity", "getJSONArrayFromStorage(): " + e.getMessage());
        }

        return jsonArray;
    }

    public void setJSONArrayInStorage(String fileName, JSONArray array, Boolean isMessages){
        String filePath;

        if(isMessages){
            filePath = mCtx.getFilesDir() + File.separator + fileName + FILE_SUFFIX;
        } else {
            filePath = mCtx.getFilesDir() + File.separator + fileName;
        }

        File file = new File(filePath);

        try {
            FileOutputStream fos = mCtx.openFileOutput(file.getName(), Context.MODE_PRIVATE);

            fos.write(array.toString().getBytes());

            fos.close();
        } catch(Exception e){
            Log.d("ChatActivity", "setJSONArrayInStorage(): " + e.getMessage());
        }
    }

    public void appendJSONObjectInStorage(String fileName, JSONObject obj, Boolean isMessages){
        String filePath;

        if(isMessages){
            filePath = mCtx.getFilesDir() + File.separator + fileName + FILE_SUFFIX;
        } else {
            filePath = mCtx.getFilesDir() + File.separator + fileName;
        }

        File file = new File(filePath);

        if(file.exists()){

            JSONArray json = getJSONArrayFromStorage(fileName, isMessages);

            try {
                json.put(obj);

                FileOutputStream fos = mCtx.openFileOutput(file.getName(), Context.MODE_PRIVATE);

                fos.write(json.toString().getBytes());

                fos.close();
            } catch(Exception e){
                Log.d("ChatActivity", "appendJSONObjectInStorage(): " +  e.getMessage());
            }
        }
    }

    public void deleteFileInStorage(String fileName, Boolean isMessages){
        String filePath;

        if(isMessages){
            filePath = mCtx.getFilesDir() + File.separator + fileName + FILE_SUFFIX;
        } else {
            filePath = mCtx.getFilesDir() + File.separator + fileName;
        }

        File file = new File(filePath);

        file.delete();
    }
}
