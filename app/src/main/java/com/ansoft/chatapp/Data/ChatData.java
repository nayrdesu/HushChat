package com.ansoft.chatapp.Data;

/**
 * Created by user on 1/30/2016.
 */
public class ChatData {
    public String msg, senderId, receiverId, timeStamp, photoLink;
    boolean isPhoto, isSeen;

    public String senderName;

    public boolean isGroup() {
        return isGroup;
    }

    public void setIsGroup(boolean isGroup) {
        this.isGroup = isGroup;
    }

    public boolean isGroup;
    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
    public boolean isSeen() {
        return isSeen;
    }

    public void setIsSeen(boolean isSeen) {
        this.isSeen = isSeen;
    }

    public String getPhotoLink() {
        return photoLink;
    }

    public void setPhotoLink(String photoLink) {
        this.photoLink = photoLink;
    }

    public String getMsg() {
        return msg;

    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isPhoto() {
        return isPhoto;
    }

    public void setIsPhoto(boolean isPhoto) {
        this.isPhoto = isPhoto;
    }

}
