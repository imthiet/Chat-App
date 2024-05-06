package com.example.chatapp.Class;

public class groupMember {
    int GroupID;
    int usnID;

    public groupMember(int GroupID, int usnID) {
        this.GroupID = GroupID;
        this.usnID = usnID;
    }

    public int getGroupID() {
        return GroupID;
    }

    public void setGroupID(int GroupID) {
        this.GroupID = GroupID;
    }

    public int getUsnID() {
        return usnID;
    }

    public void setUsnID(int usnID) {
        this.usnID = usnID;
    }

}

