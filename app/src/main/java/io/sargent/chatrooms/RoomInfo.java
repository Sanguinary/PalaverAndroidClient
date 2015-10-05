package io.sargent.chatrooms;


public class RoomInfo {
    public String roomName;
    public String password;

    public RoomInfo(String nRoom, String nPassword){
        roomName = nRoom;
        password = nPassword;
    }

    @Override
    public String toString(){
        return "RoomInfo: roomName=" + roomName + ", password=" + password;
    }
}
