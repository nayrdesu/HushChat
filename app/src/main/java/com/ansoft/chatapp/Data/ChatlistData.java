package com.ansoft.chatapp.Data;

import com.parse.ParseObject;

/**
 * Created by user on 1/31/2016.
 */
public class ChatlistData {

    public ParseObject chat;

    public ParseObject getChat() {
        return chat;
    }

    public void setChat(ParseObject chat) {
        this.chat = chat;
    }

    public String otherId;
    public String lastMsg;
    public String timeStamp;
    public String groupName;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setIsGroup(boolean isGroup) {
        this.isGroup = isGroup;
    }

    public boolean isGroup;

    public boolean isSeen;

    public String getOtherId() {
        return otherId;
    }

    public void setOtherId(String otherId) {
        this.otherId = otherId;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setIsSeen(boolean isSeen) {
        this.isSeen = isSeen;
    }
}
