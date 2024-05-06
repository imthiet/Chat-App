package com.example.chatapp.Class;

public class Friend {
    int FriendShipID,usnID1,usnID2;
    String status;
    String timeStamp;

    public Friend(int FriendShipID, int usnID1, int usnID2, String status, String timeStamp) {
        this.FriendShipID = FriendShipID;
        this.usnID1 = usnID1;
        this.usnID2 = usnID2;
        this.status = status;
        this.timeStamp = timeStamp;
    }

    public int getFriendShipID() {
        return FriendShipID;
    }

    public void setFriendShipID(int FriendShipID) {
        this.FriendShipID = FriendShipID;
    }

    public int getUsnID1() {
        return usnID1;
    }

    public void setUsnID1(int usnID1) {
        this.usnID1 = usnID1;
    }

    public int getUsnID2() {
        return usnID2;
    }

    public void setUsnID2(int usnID2) {
        this.usnID2 = usnID2;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

}
