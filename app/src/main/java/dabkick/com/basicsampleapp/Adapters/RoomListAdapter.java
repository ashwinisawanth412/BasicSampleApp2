package dabkick.com.basicsampleapp.Adapters;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;
import java.util.logging.Handler;

import dabkick.com.basicsampleapp.ChatRoomFragment;
import dabkick.com.basicsampleapp.HomePageActivity;
import dabkick.com.basicsampleapp.Model.Room;
import dabkick.com.basicsampleapp.R;
import dabkick.com.basicsampleapp.SplashScreenActivity;

public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.RoomHolder> {

    private volatile List<Room> roomInfoList;
    private Activity context;

    public RoomListAdapter(List<Room> roomList, Activity activity) {
        this.roomInfoList = roomList;
        this.context = activity;
    }

    @NonNull
    @Override
    public RoomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.room_list_item, parent, false);

        return new RoomHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomHolder holder, int position) {
        Room room = roomInfoList.get(position);
        String roomName = room.getRoomName();
        if (roomName != null)
            holder.roomName.setText(roomName);


        int count = room.getUnreadMsgCount();
        if (count == 0) {
            holder.unreadMsgCount.setVisibility(View.GONE);
            holder.roomName.setTypeface(null, Typeface.NORMAL);
        } else {
            holder.roomName.setTypeface(null, Typeface.BOLD);
            if (count > 99) {
                holder.unreadMsgCount.setText("99+");
            } else {
                holder.unreadMsgCount.setText("" + count);
            }

            holder.unreadMsgCount.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(room.getLatestMsg())) {
            holder.roomLatestMsg.setVisibility(View.VISIBLE);
            holder.roomLatestMsg.setText(room.getLatestMsg());
        } else {
            holder.roomLatestMsg.setVisibility(View.GONE);
        }

        if (SplashScreenActivity.dkLiveChat.isSubscribed(roomName)) {
            holder.roomName.setTextColor(Color.BLACK);
            holder.roomName.setTypeface(null, Typeface.BOLD_ITALIC);
        } else {
            holder.roomName.setTextColor(Color.DKGRAY);
        }
    }

    public Room getRoomItem(String roomName) {
        if (roomName != null && !roomName.isEmpty()) {
            for (int i = 0; i < roomInfoList.size(); i++) {
                Room room = roomInfoList.get(i);
                if (room.getRoomName().equalsIgnoreCase(roomName)) {
                    Log.d("RomListAdapter", "returning room");
                    return room;
                }
            }
        }
        Log.d("RomListAdapter", "returning null");
        return null;
    }

    @Override
    public int getItemCount() {
        return roomInfoList.size();
    }

    public class RoomHolder extends RecyclerView.ViewHolder {
        public AppCompatTextView roomName, unreadMsgCount, roomLatestMsg;

        public RoomHolder(@NonNull View itemView) {
            super(itemView);
            roomName = itemView.findViewById(R.id.room_name);
            unreadMsgCount = itemView.findViewById(R.id.unread_count);
            roomLatestMsg = itemView.findViewById(R.id.room_latest_msg);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (context.getClass() == HomePageActivity.class) {
                        if (SplashScreenActivity.dkLiveChat.isSubscribed(roomName.getText().toString()))
                            ((HomePageActivity) context).showSubscribedUserBottomSheet(roomName.getText().toString());
                        else
                            ((HomePageActivity) context).showUnsubscribedUserBottomSheet(roomName.getText().toString());

                    }
                }
            });
        }
    }

    public void addRoom(Room roomInfo) {
        roomInfoList.add(roomInfo);
        notifyItemInserted(roomInfoList.size() - 1);
    }

    public void updateRoomUponNewMsg(Room room) {
        if (roomInfoList.contains(room)) {
            if (room.getUnreadMsgCount() > 0) {
                Collections.swap(roomInfoList, roomInfoList.indexOf(room), 0);
                notifyDataSetChanged();
            }
        }
    }

    public void moveRoomToTop(Room room) {
        if (roomInfoList.contains(room)) {
            Collections.swap(roomInfoList, roomInfoList.indexOf(room), 0);
            notifyDataSetChanged();
        }
    }

    public void updateRoomUponUnsubscribe(Room room) {
        if (roomInfoList.contains(room)) {
            Collections.swap(roomInfoList, roomInfoList.indexOf(room), roomInfoList.size() - 1);
            notifyDataSetChanged();
        }
    }

    public void enterRoomOnCreation(String roomName) {
        ChatRoomFragment chatRoom = ChatRoomFragment.newInstance(roomName, true);
        android.support.v4.app.FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frag_container, chatRoom);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void setLatestRoomMsg(String roomName, String msg/*, timestamp to be passed here*/) {
        if (roomInfoList.contains(roomName)) {
            Room room = getRoomItem(roomName);
            room.setLatestMsg(msg);
            roomInfoList.remove(room);
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    roomInfoList.add(0, room);
                    notifyDataSetChanged();

                }
            }, 200);
        }
    }
}
