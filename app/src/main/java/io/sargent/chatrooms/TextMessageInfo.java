package io.sargent.chatrooms;

public class TextMessageInfo {
    protected String message;
    protected String user;

    public TextMessageInfo(String nUser, String nMessage){
        message = nMessage;
        user = nUser;
    }

    //Accessors
    public String getMessage(){
        return message;
    }
    public String getUser(){
        return user;
    }
}
