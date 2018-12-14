package dabkick.com.basicsampleapp.Adapters;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dabkick.com.basicsampleapp.ChatRoomFragment;
import dabkick.com.basicsampleapp.Model.Room;
import dabkick.com.basicsampleapp.R;

public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.RoomHolder> {

    private List<Room> roomInfoList = new ArrayList<>();
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
    }

    public Room getRoomItem(String roomName) {
        if (roomName != null && !roomName.isEmpty()) {
            for (Room room : roomInfoList) {
                if (room.getRoomName().equalsIgnoreCase(roomName)) {
                    return room;
                }
            }
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return roomInfoList.size();
    }

    public class RoomHolder extends RecyclerView.ViewHolder {
        public AppCompatTextView roomName, unreadMsgCount;

        public RoomHolder(@NonNull View itemView) {
            super(itemView);
            roomName = itemView.findViewById(R.id.room_name);
            unreadMsgCount = itemView.findViewById(R.id.unread_count);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChatRoomFragment chatRoom = ChatRoomFragment.newInstance(roomName.getText().toString());
                    android.support.v4.app.FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frag_container, chatRoom);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
        }
    }

    public void addRoom(Room roomInfo) {
        roomInfoList.add(roomInfo);
        notifyItemInserted(roomInfoList.size() - 1);
    }

    public void updateRoom(Room room) {
        if(roomInfoList.contains(room))
            roomInfoList.remove(room);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                roomInfoList.set(0, room);
                notifyItemChanged(0, room);
            }
        }, 500);
    }

}
