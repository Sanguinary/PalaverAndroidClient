package io.sargent.chatrooms;


public class RoomInfo {
    public String roomName;

    public RoomInfo(String nRoom){
        roomName = nRoom;
    }

    @Override
    public String toString(){
        return "RoomInfo: roomName=" + roomName;
    }
}
