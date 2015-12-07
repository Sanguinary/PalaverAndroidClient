package io.sargent.chatrooms;

public class TextMessageInfo {
    protected String message;
    protected String user;
    protected boolean sender;
    protected String color;

    public TextMessageInfo(String nUser, String nMessage, String nColor, boolean nSender){
        message = nMessage;
        user = nUser;
        sender = nSender;
        color = nColor;
    }

    //Accessors
    public String getColor(){return color;}
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
