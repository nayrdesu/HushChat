package com.ansoft.chatapp.Constants;

import com.parse.ParseObject;

/**
 * Created by user on 1/27/2016.
 */
public class PC {

    public static final String KEY_DISPLAY_NAME="DisplayName";
    public static final String KEY_PHOTO_NAME="Photo.JPG";
    public static final String KEY_PROFILE_PHOTO="ProfilePhoto";
    public static final String KEY_FRIENDS_ID="friendsid";
    public static final String KEY_USER_ISONLINE="isonline";
    public static final String KEY_USER_STATUS="userstatus";
    public static final String KEY_CHAT_OBJECT="chat";
    public static final String KEY_CHAT_MESSAGES="messages";
    public static final String KEY_CHAT_UNIQUE_ID="uniqueid";
    public static final String KEY_CHAT_LAST_MSGID="lastmsgid";
    public static final String KEY_CHAT_IS_SEEN="isSeen";
    public static final String KEY_CHAT_BETWEEN="chatBtns";
    public static final String KEY_CHAT_IS_GROUP="isGroup";
    public static final String KEY_CHAT_LAST_MSG_SENDER_NAME="lastMsgSenderName";
    public static final String KEY_CHAT_GROUP_NAME="groupName";
    public static final String KEY_LAST_ONLINE="lastOnline";
    public static final String KEY_DEFAULT_PROFILE_PHOTO_LINK="http://pasteboard.co/1c0QCnTG.png";

    public static ParseObject currentChat;

    public static ParseObject getCurrentChat() {
        return currentChat;
    }

    public static void setCurrentChat(ParseObject currentChat) {
        PC.currentChat = currentChat;
    }
}
