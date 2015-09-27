package io.sargent.chatrooms;

public class TextMessageInfo {
    protected String message;
    protected String user;
    protected boolean sender;

    public TextMessageInfo(String nUser, String nMessage, boolean nSender){
        message = nMessage;
        user = nUser;
        sender = nSender;
    }

    //Accessors
    public String getMessage(){
        return message;
    }
    public String getUser(){
        return user;
    }
    public boolean isSender() {
        return sender;
    }
}
