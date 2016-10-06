package com.ansoft.chatapp.Data;


public class FriendList {
    String profilePhoto, displayName, userStatus, userId;
    String mainString;

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public FriendList() {
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public FriendList(String mainString) {
        this.mainString = mainString;
    }

    public void DecomPress(){
        String[] array=mainString.split("$");
        this.profilePhoto=array[0];
        this.displayName=array[1];
        this.userStatus=array[2];
        this.userId=array[3];
    }

    public String getMainString() {
        return mainString;
    }

    public void Compress(){
        this.mainString=displayName+"-"+userStatus+"-"+userId+"-"+profilePhoto;
    }
}
