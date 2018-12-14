package dabkick.com.basicsampleapp.Model;

import java.util.ArrayList;
import java.util.List;

import com.dabkick.engine.Public.MessageInfo;

public class Room {
    private String roomName;
    List<MessageInfo> unreadMsgList;

    public Room() {
        unreadMsgList = new ArrayList<MessageInfo>();
    }

    public String getRoomName() {
        return this.roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void addUnreadMsg(MessageInfo msg) {
        if (this.unreadMsgList != null)
            this.unreadMsgList.add(msg);
    }

    public void clearUnreadMsgList() {
        if (this.unreadMsgList != null)
            this.unreadMsgList.clear();
    }

    public int getUnreadMsgCount() {
        if (this.unreadMsgList == null)
            return 0;
        return this.unreadMsgList.size();
    }

}
