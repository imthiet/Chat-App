package com.example.chatapp.Class;

public class Msg {
    int MsgID,SenderID,ReceiveID;
    String MsgText,MsgType;
    String timeStamp;

    public Msg(int MsgID, int SenderID, int ReceiveID, String MsgText, String MsgType, String timeStamp) {
        this.MsgID = MsgID;
        this.SenderID = SenderID;
        this.ReceiveID = ReceiveID;
        this.MsgText = MsgText;
        this.MsgType = MsgType;
        this.timeStamp = timeStamp;
    }

    public int getMsgID() {
        return MsgID;
    }

    public void setMsgID(int MsgID) {
        this.MsgID = MsgID;
    }

    public int getSenderID() {
        return SenderID;
    }

    public void setSenderID(int SenderID) {
        this.SenderID = SenderID;
    }

    public int getReceiveID() {
        return ReceiveID;
    }

    public void setReceiveID(int ReceiveID) {
        this.ReceiveID = ReceiveID;
    }

    public String getMsgText() {
        return MsgText;
    }

    public void setMsgText(String MsgText) {
        this.MsgText = MsgText;
    }

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String MsgType) {
        this.MsgType = MsgType;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

}


